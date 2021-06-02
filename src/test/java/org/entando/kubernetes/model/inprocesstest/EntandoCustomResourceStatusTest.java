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

package org.entando.kubernetes.model.inprocesstest;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import io.fabric8.kubernetes.api.model.StatusBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import org.entando.kubernetes.model.SampleWriter;
import org.entando.kubernetes.model.capability.CapabilityScope;
import org.entando.kubernetes.model.common.AbstractServerStatus;
import org.entando.kubernetes.model.common.DbmsVendor;
import org.entando.kubernetes.model.common.EntandoControllerFailureBuilder;
import org.entando.kubernetes.model.common.EntandoCustomResourceStatus;
import org.entando.kubernetes.model.common.EntandoDeploymentPhase;
import org.entando.kubernetes.model.common.EntandoResourceRequirements;
import org.entando.kubernetes.model.common.ExposedServerStatus;
import org.entando.kubernetes.model.common.InternalServerStatus;
import org.entando.kubernetes.model.keycloakserver.EntandoKeycloakServer;
import org.entando.kubernetes.model.keycloakserver.EntandoKeycloakServerSpec;
import org.entando.kubernetes.model.keycloakserver.StandardKeycloakImage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags({@Tag("in-process"), @Tag("pre-deployment")})
class EntandoCustomResourceStatusTest {

    private static void populateStatus(AbstractServerStatus serverStatus) {
        serverStatus.setDeploymentName("my-deployment");
        serverStatus.setServiceName("my-service");
        serverStatus.setAdminSecretName("my-admin-secret");
        serverStatus.putDerivedDeploymentParameter("database-name", "my-database");
        serverStatus.putPodPhase("initPod1", "Completed");
        serverStatus.putPodPhase("pod1", "Running");
        serverStatus.putPersistentVolumeClaimPhase("pvc1", "Bound");
        serverStatus.finish();
    }

    @Test
    void testSerializeDeserialize() {
        InternalServerStatus internalServerStatus = new InternalServerStatus();
        internalServerStatus.setQualifier("db");
        populateStatus(internalServerStatus);
        ExposedServerStatus zebServerStatus = new ExposedServerStatus();
        zebServerStatus.setQualifier("zeb");
        zebServerStatus.setIngressName("zy-ingress");
        populateStatus(zebServerStatus);
        long start = System.currentTimeMillis();
        await().until(() -> System.currentTimeMillis() - start > 1000);
        ExposedServerStatus exposedServerStatus = new ExposedServerStatus();
        exposedServerStatus.setQualifier("web");
        exposedServerStatus.setIngressName("my-ingress");
        exposedServerStatus.setExternalBaseUrl("http://myhost.com/path");
        exposedServerStatus.finishWith(
                new EntandoControllerFailureBuilder().withFailedObjectType("Wrong").withFailedObjectName("Wrong")
                        .withException(new KubernetesClientException("Wrong", 403,
                                new StatusBuilder().withMessage("Ingress failed").withNewDetails()
                                        .withKind("Ingress")
                                        .withName("MyIngress")
                                        .endDetails().build())).build()
        );
        populateStatus(exposedServerStatus);
        EntandoKeycloakServer keycloakServer = new EntandoKeycloakServer();
        keycloakServer.getMetadata().setGeneration(3L);
        keycloakServer
                .setSpec(
                        new EntandoKeycloakServerSpec(null, StandardKeycloakImage.KEYCLOAK, null, null, null, DbmsVendor.ORACLE, null, null,
                                1, true,
                                "my-service-account",
                                Collections.emptyList(), new EntandoResourceRequirements(), null, CapabilityScope.CLUSTER));
        keycloakServer.getMetadata().setName("test-keycloak");
        keycloakServer.setStatus(new EntandoCustomResourceStatus());
        keycloakServer.getStatus().putServerStatus(internalServerStatus);
        keycloakServer.getStatus().putServerStatus(exposedServerStatus);
        keycloakServer.getStatus().putServerStatus(zebServerStatus);
        Path sample = SampleWriter.writeSample(Paths.get("target"), keycloakServer);
        EntandoKeycloakServer actual = SampleWriter.readSample(sample, EntandoKeycloakServer.class);
        assertThat(actual.getStatus().getServerStatus("db").get().getDeploymentName(), is("my-deployment"));
        ExposedServerStatus actualFinalStatus = (ExposedServerStatus) actual.getStatus().findCurrentServerStatus().get();
        assertThat(actualFinalStatus.getQualifier(), is("web"));
        assertThat(actualFinalStatus.getFinished(), is(notNullValue()));
        assertThat(actualFinalStatus.getServiceName(), is("my-service"));
        assertThat(actualFinalStatus.getAdminSecretName().get(), is("my-admin-secret"));
        assertThat(actualFinalStatus.getIngressName(), is("my-ingress"));
        assertThat(actualFinalStatus.getExternalBaseUrl(), is("http://myhost.com/path"));
        assertThat(actualFinalStatus.getPodPhases().get("initPod1"), is("Completed"));
        assertThat(actualFinalStatus.getPodPhases().get("pod1"), is("Running"));
        assertThat(actualFinalStatus.getPersistentVolumeClaimPhases().get("pvc1"), is("Bound"));
        assertThat(actualFinalStatus.getDerivedDeploymentParameters().get("database-name"), is("my-database"));
        assertThat(actualFinalStatus.getEntandoControllerFailure().getFailedObjectType(), is("Ingress"));
        assertThat(actualFinalStatus.getEntandoControllerFailure().getFailedObjectName(), is("MyIngress"));
        assertThat(actualFinalStatus.getEntandoControllerFailure().getMessage(), is("Ingress failed"));
        assertThat(actualFinalStatus.getEntandoControllerFailure().getDetailMessage(),
                containsString("io.fabric8.kubernetes.client.KubernetesClientException"));
        assertThat(actual.getStatus().calculateFinalPhase(), is(EntandoDeploymentPhase.FAILED));
        assertThat(actual.getStatus().getServerStatuses().size(), is(3));

    }
}
