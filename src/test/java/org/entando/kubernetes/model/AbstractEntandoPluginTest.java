/*
 *
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 */

package org.entando.kubernetes.model;

import static org.entando.kubernetes.model.plugin.PluginSecurityLevel.STRICT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import java.util.Arrays;
import java.util.Collections;
import org.entando.kubernetes.model.common.DbmsVendor;
import org.entando.kubernetes.model.common.EntandoDeploymentPhase;
import org.entando.kubernetes.model.common.ExpectedRole;
import org.entando.kubernetes.model.common.Permission;
import org.entando.kubernetes.model.common.ServerStatus;
import org.entando.kubernetes.model.plugin.EntandoPlugin;
import org.entando.kubernetes.model.plugin.EntandoPluginBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Sonar doesn't pick up that this class is extended in other packages
@SuppressWarnings("java:S5786")
public abstract class AbstractEntandoPluginTest implements CustomResourceTestUtil {

    protected static final String MY_PLUGIN = "my-plugin";
    protected static final String MY_NAMESPACE = TestConfig.calculateNameSpace("my-namespace");
    private static final String MY_CLUSTER_INFRASTRUCTURE = "my-cluster-infrastructure";
    private static final String MY_KEYCLOAK_NAME = "my-keycloak-name";
    private static final String MY_KEYCLOAK_REALM = "my-keycloak-realm";
    private static final String MY_KEYCLOAK_NAME_SPACE = "my-keycloak-namespace";
    private static final String MYHOST_COM = "myhost.com";
    private static final String MY_TLS_SECRET = "my-tls-secret";
    private static final String IMAGE = "entando/someplugin:1.0.2";
    public static final String PRIMARY_TEST_SPEC = "primary";
    private static final String SOME_CONNECTION = "some-connection";
    private static final String INGRESS_PATH = "/plugsy";
    private static final String ACTUATOR_HEALTH = "/actuator/health";
    private static final String ENTANDO_APP = "entando-app";
    private static final String SUPERUSER = "superuser";
    private static final String MY_COMPANION_CONTAINER = "my-companion-container";
    private static final String ADMIN = "admin";
    private static final String ADMINISTRATOR = "Administrator";
    private static final String PARAMETER_NAME = "env";
    private static final String PARAMETER_VALUE = "B";
    private static final String MY_PUBLIC_CLIENT = "my-public-client";

    @BeforeEach
    public void deleteEntandoPlugins() {
        prepareNamespace(entandoPlugins(), MY_NAMESPACE);
    }

    @Test
    void testCreateEntandoPlugin() {
        //Given
        EntandoPlugin entandoPlugin = new EntandoPluginBuilder()
                .withNewMetadata().withName(MY_PLUGIN)
                .withNamespace(MY_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withDbms(DbmsVendor.MYSQL)
                .withTenantCode(null)
                .withImage(IMAGE)
                .addNewConnectionConfigName(SOME_CONNECTION)
                .withReplicas(5)
                .withIngressPath(INGRESS_PATH)
                .withHealthCheckPath(ACTUATOR_HEALTH)
                .withIngressHostName(MYHOST_COM)
                .withTlsSecretName(MY_TLS_SECRET)
                .addNewPermission(ENTANDO_APP, SUPERUSER)
                .addNewRole(ADMIN, ADMINISTRATOR)
                .addNewCompanionContainer(MY_COMPANION_CONTAINER)
                .addToEnvironmentVariables(PARAMETER_NAME, PARAMETER_VALUE)
                .withSecurityLevel(STRICT)
                .withNewKeycloakToUse()
                .withNamespace(MY_KEYCLOAK_NAME_SPACE)
                .withName(MY_KEYCLOAK_NAME)
                .withRealm(MY_KEYCLOAK_REALM)
                .withPublicClientId(MY_PUBLIC_CLIENT)
                .endKeycloakToUse()
                .endSpec()
                .build();
        entandoPlugins().inNamespace(MY_NAMESPACE).create(entandoPlugin);
        //When
        EntandoPlugin actual = entandoPlugins().inNamespace(MY_NAMESPACE).withName(MY_PLUGIN).get();
        //Then
        assertThat(actual.getSpec().getDbms().get(), is(DbmsVendor.MYSQL));
        assertThat(actual.getSpec().getImage(), is(IMAGE));
        assertThat(actual.getSpec().getTenantCode(), is(PRIMARY_TEST_SPEC));
        verifyKeycloakToUse(actual);
        assertThat(actual.getSpec().getTlsSecretName().get(), is(MY_TLS_SECRET));
        assertThat(actual.getTlsSecretName().get(), is(MY_TLS_SECRET));
        assertThat(actual.getSpec().getIngressHostName().get(), is(MYHOST_COM));
        assertThat(actual.getIngressHostName().get(), is(MYHOST_COM));
        assertThat(actual.getSpec().getConnectionConfigNames(), is(Arrays.asList(SOME_CONNECTION)));
        assertThat(actual.getSpec().getCompanionContainers(), is(Arrays.asList(MY_COMPANION_CONTAINER)));
        assertThat(actual.getSpec().getPermissions().get(0).getClientId(), is(ENTANDO_APP));
        assertThat(actual.getSpec().getPermissions().get(0).getRole(), is(SUPERUSER));
        assertThat(findParameter(actual.getSpec(), PARAMETER_NAME).get().getValue(), is(PARAMETER_VALUE));
        assertThat(actual.getSpec().getRoles().get(0).getCode(), is(ADMIN));
        assertThat(actual.getSpec().getRoles().get(0).getName(), is(ADMINISTRATOR));
        assertThat(actual.getSpec().getSecurityLevel().get(), is(STRICT));
        assertThat(actual.getSpec().getIngressPath(), is(INGRESS_PATH));
        assertThat(actual.getSpec().getHealthCheckPath(), is(ACTUATOR_HEALTH));
        assertThat(actual.getSpec().getReplicas().get(), is(5));
        assertThat(actual.getMetadata().getName(), is(MY_PLUGIN));
        assertThat(actual.getStatus(), is(notNullValue()));
    }

    private void verifyKeycloakToUse(EntandoPlugin actual) {
        assertThat(actual.getSpec().getKeycloakToUse().get().getName(), is(MY_KEYCLOAK_NAME));
        assertThat(actual.getSpec().getKeycloakToUse().get().getNamespace().get(), is(MY_KEYCLOAK_NAME_SPACE));
        assertThat(actual.getSpec().getKeycloakToUse().get().getRealm().get(), is(MY_KEYCLOAK_REALM));
        assertThat(actual.getSpec().getKeycloakToUse().get().getPublicClientId().get(), is(MY_PUBLIC_CLIENT));
    }

    @Test
    void testEditEntandoPlugin() {
        //Given
        EntandoPlugin entandoPlugin = new EntandoPluginBuilder()
                .withNewMetadata()
                .withName(MY_PLUGIN)
                .withNamespace(MY_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withDbms(DbmsVendor.POSTGRESQL)
                .withImage("entando/enoatherplugin:1.0.2")
                .addNewConnectionConfigName("another-connection")
                .withReplicas(5)
                .withIngressPath(INGRESS_PATH)
                .withHealthCheckPath("/actuator/unhealth")
                .addNewPermission("entando-usermgment", "subuser")
                .addNewRole("user", "User")
                .addNewCompanionContainer(MY_COMPANION_CONTAINER)
                .addToEnvironmentVariables(PARAMETER_NAME, "A")
                .withSecurityLevel(STRICT)
                .withNewKeycloakToUse()
                .withNamespace("somenamespace")
                .withName("another-keycloak")
                .withRealm("somerealm")
                .withPublicClientId("some-public-client")
                .endKeycloakToUse()
                .endSpec()
                .build();
        //When
        final EntandoPluginBuilder toEdit = new EntandoPluginBuilder(
                entandoPlugins().inNamespace(MY_NAMESPACE).create(entandoPlugin));
        EntandoPlugin actual = entandoPlugins().inNamespace(MY_NAMESPACE).withName(MY_PLUGIN).patch(toEdit
                .editMetadata().addToLabels("my-label", "my-value")
                .endMetadata()
                .editSpec()
                .withDbms(DbmsVendor.MYSQL)
                .withImage(IMAGE)
                .withTenantCode("tenant1")
                .withConnectionConfigNames(Arrays.asList(SOME_CONNECTION))
                .withReplicas(5)
                .withHealthCheckPath(ACTUATOR_HEALTH)
                .withIngressHostName(MYHOST_COM)
                .withTlsSecretName(MY_TLS_SECRET)
                .withPermissions(Arrays.asList(new Permission(ENTANDO_APP, SUPERUSER)))
                .withRoles(Arrays.asList(new ExpectedRole(ADMIN, ADMINISTRATOR)))
                .withEnvironmentVariables(Collections.singletonList(new EnvVar(PARAMETER_NAME, PARAMETER_VALUE, null)))
                .withSecurityLevel(STRICT)
                .editKeycloakToUse()
                .withNamespace(MY_KEYCLOAK_NAME_SPACE)
                .withName(MY_KEYCLOAK_NAME)
                .withRealm(MY_KEYCLOAK_REALM)
                .withPublicClientId(MY_PUBLIC_CLIENT)
                .endKeycloakToUse()
                .endSpec()
                .build());
        actual.getStatus().putServerStatus(new ServerStatus("some-qualifier"));
        actual.getStatus().putServerStatus(new ServerStatus("some-other-qualifier"));
        actual.getStatus().putServerStatus(new ServerStatus("some-qualifier"));
        actual.getStatus().putServerStatus(new ServerStatus("another-qualifier"));
        actual.getStatus().updateDeploymentPhase(EntandoDeploymentPhase.STARTED, 5L);
        actual = entandoPlugins().inNamespace(actual.getMetadata().getNamespace()).updateStatus(actual);

        //Then
        assertThat(actual.getSpec().getDbms().get(), is(DbmsVendor.MYSQL));
        assertThat(actual.getSpec().getImage(), is(IMAGE));
        assertThat(actual.getSpec().getTenantCode(), is("tenant1"));
        verifyKeycloakToUse(actual);
        assertThat(actual.getSpec().getTlsSecretName().get(), is(MY_TLS_SECRET));
        assertThat(actual.getTlsSecretName().get(), is(MY_TLS_SECRET));
        assertThat(actual.getSpec().getIngressHostName().get(), is(MYHOST_COM));
        assertThat(actual.getIngressHostName().get(), is(MYHOST_COM));
        assertThat(actual.getSpec().getConnectionConfigNames(), is(Arrays.asList(SOME_CONNECTION)));
        assertThat(actual.getSpec().getConnectionConfigNames(), is(Arrays.asList(SOME_CONNECTION)));
        assertThat(actual.getSpec().getPermissions().get(0).getClientId(), is(ENTANDO_APP));
        assertThat(actual.getSpec().getPermissions().get(0).getRole(), is(SUPERUSER));
        assertThat(actual.getSpec().getRoles().get(0).getCode(), is(ADMIN));
        assertThat(actual.getSpec().getRoles().get(0).getName(), is(ADMINISTRATOR));
        assertThat(actual.getSpec().getSecurityLevel().get(), is(STRICT));
        assertThat(findParameter(actual.getSpec(), PARAMETER_NAME).get().getValue(), is(PARAMETER_VALUE));
        assertThat(actual.getSpec().getReplicas().get(), is(5));
        assertThat(actual.getMetadata().getName(), is(MY_PLUGIN));
        assertThat("the status reflects", actual.getStatus().getServerStatus("some-qualifier").isPresent());
        assertThat("the status reflects", actual.getStatus().getServerStatus("some-other-qualifier").isPresent());
        assertThat("the status reflects", actual.getStatus().getServerStatus("another-qualifier").isPresent());
        assertThat(actual.getStatus().getObservedGeneration(), is(5L));
        assertThat(actual.getStatus().getPhase(), is(EntandoDeploymentPhase.STARTED));
    }

    protected MixedOperation<EntandoPlugin, KubernetesResourceList<EntandoPlugin>, Resource<EntandoPlugin>> entandoPlugins() {
        return getClient().customResources(EntandoPlugin.class);
    }

}
