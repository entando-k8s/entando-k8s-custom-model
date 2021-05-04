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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import org.entando.kubernetes.model.capability.CapabilityProvisioningStrategy;
import org.entando.kubernetes.model.capability.CapabilityScope;
import org.entando.kubernetes.model.capability.ProvidedCapability;
import org.entando.kubernetes.model.capability.ProvidedCapabilityBuilder;
import org.entando.kubernetes.model.capability.StandardCapability;
import org.entando.kubernetes.model.capability.StandardCapabilityImplementation;
import org.entando.kubernetes.model.common.ResourceReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Sonar doesn't pick up that this class is extended in other packages
@SuppressWarnings("java:S5786")
public abstract class AbstractProvidedCapabilityTest implements CustomResourceTestUtil {

    protected static final String MY_NAMESPACE = TestConfig.calculateNameSpace("my-namespace");
    public static final String MY_ADMIN_SECRET = "my-admin-secret";
    public static final String MY_CAPABILITY = "my-capability";

    @BeforeEach
    public void deleteEntandoAppPluginLinks() {

        prepareNamespace(getClient().customResources(ProvidedCapability.class), MY_NAMESPACE);
    }

    @Test
    void testCreateProvidedCapability() {
        //Given
        final ProvidedCapability providedCapability = new ProvidedCapabilityBuilder()
                .withNewMetadata()
                .withName(MY_CAPABILITY)
                .withNamespace(MY_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withCapability(StandardCapability.SSO)
                .withImplementation(StandardCapabilityImplementation.KEYCLOAK)
                .withCapabilityRequirementScope(CapabilityScope.CLUSTER)
                .withProvisioningStrategy(CapabilityProvisioningStrategy.USE_EXTERNAL)
                .withExternallyProvisionedService("keycloak.host.com", 80, MY_ADMIN_SECRET)
                .withSpecifiedCapability(new ResourceReference(MY_NAMESPACE, "my-keycloak"))
                .withCapabilityParameters(Collections.singletonMap("frontendUrl", "http://somehost.com/auth"))

                .endSpec()
                .build();

        getClient().customResources(ProvidedCapability.class).inNamespace(MY_NAMESPACE)
                .create(new ProvidedCapabilityBuilder().withMetadata(providedCapability.getMetadata())
                        .withSpec(providedCapability.getSpec())
                        .build());
        //When

        ProvidedCapability actual = getClient().customResources(ProvidedCapability.class).inNamespace(MY_NAMESPACE).withName(MY_CAPABILITY)
                .get();
        //Then
        assertThat(actual.getSpec().getCapability(), is(StandardCapability.SSO));
        assertThat(actual.getSpec().getImplementation().get(), is(StandardCapabilityImplementation.KEYCLOAK));
        assertThat(actual.getSpec().getScope().get(), is(CapabilityScope.CLUSTER));
        assertThat(actual.getSpec().getProvisioningStrategy().get(), is(CapabilityProvisioningStrategy.USE_EXTERNAL));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getHost(), is("keycloak.host.com"));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getPort().get(), is(80));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getAdminSecretName(), is(MY_ADMIN_SECRET));
        assertThat(actual.getSpec().getSpecifiedCapability().get().getName(), is("my-keycloak"));
        assertThat(actual.getSpec().getSpecifiedCapability().get().getNamespace().get(), is(MY_NAMESPACE));
    }

    @Test
    void testEditProvidedCapability() {
        //Given
        final ProvidedCapability providedCapability = new ProvidedCapabilityBuilder()
                .withNewMetadata()
                .withName(MY_CAPABILITY)
                .withNamespace(MY_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withCapability(StandardCapability.DBMS)
                .withImplementation(StandardCapabilityImplementation.MYSQL)
                .withCapabilityRequirementScope(CapabilityScope.NAMESPACE)
                .withProvisioningStrategy(CapabilityProvisioningStrategy.DELEGATE_TO_OPERATOR)
                .withExternallyProvisionedService("keycloak.somehost.com", 81, "some-admin-secret")
                .withSpecifiedCapability(new ResourceReference("some-namespace", "some-keycloak"))
                .withCapabilityParameters(Collections.singletonMap("frontendUrl", "http://somehost.com/auth"))

                .endSpec()
                .build();
        //When
        //We are not using the mock server here because of a known bug

        getClient().customResources(ProvidedCapability.class).inNamespace(MY_NAMESPACE).create(providedCapability);

        ProvidedCapability actual = getClient().customResources(ProvidedCapability.class).inNamespace(MY_NAMESPACE).withName(MY_CAPABILITY)
                .patch(new ProvidedCapabilityBuilder(
                        getClient().customResources(ProvidedCapability.class).inNamespace(MY_NAMESPACE).withName(MY_CAPABILITY).fromServer()
                                .get())
                        .editMetadata().addToLabels("my-label", "my-value")
                        .endMetadata()
                        .editSpec()
                        .withCapability(StandardCapability.SSO)
                        .withImplementation(StandardCapabilityImplementation.KEYCLOAK)
                        .withCapabilityRequirementScope(CapabilityScope.CLUSTER)
                        .withProvisioningStrategy(CapabilityProvisioningStrategy.USE_EXTERNAL)
                        .withExternallyProvisionedService("keycloak.host.com", 80, MY_ADMIN_SECRET)
                        .withSpecifiedCapability(new ResourceReference(MY_NAMESPACE, "my-keycloak"))
                        .withCapabilityParameters(Collections.singletonMap("frontendUrl", "http://somehost.com/auth"))
                        .endSpec()
                        .build());
        //Then
        //Then
        assertThat(actual.getSpec().getCapability(), is(StandardCapability.SSO));
        assertThat(actual.getSpec().getImplementation().get(), is(StandardCapabilityImplementation.KEYCLOAK));
        assertThat(actual.getSpec().getScope().get(), is(CapabilityScope.CLUSTER));
        assertThat(actual.getSpec().getProvisioningStrategy().get(), is(CapabilityProvisioningStrategy.USE_EXTERNAL));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getHost(), is("keycloak.host.com"));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getPort().get(), is(80));
        assertThat(actual.getSpec().getExternallyProvisionedService().get().getAdminSecretName(), is(MY_ADMIN_SECRET));
        assertThat(actual.getSpec().getSpecifiedCapability().get().getName(), is("my-keycloak"));
        assertThat(actual.getSpec().getSpecifiedCapability().get().getNamespace().get(), is(MY_NAMESPACE));
    }

}
