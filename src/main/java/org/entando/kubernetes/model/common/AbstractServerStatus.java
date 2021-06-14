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

package org.entando.kubernetes.model.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@JsonSerialize
@JsonDeserialize
@JsonTypeInfo(
        use = Id.NAME,
        include = As.EXISTING_PROPERTY,
        property = "type"

)
@JsonSubTypes({
        @Type(value = ExposedServerStatus.class, name = "ExposedServerStatus"),
        @Type(value = InternalServerStatus.class, name = "InternalServerStatus"),
})
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE)
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractServerStatus implements Serializable {

    private String qualifier;
    private String type = getClass().getSimpleName();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "GMT")
    private Date started = new Date();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "GMT")
    private Date finished;
    private String serviceName;
    private String deploymentName;
    private String adminSecretName;
    private Map<String, String> podPhases;
    private Map<String, String> persistentVolumeClaimPhases;
    private Map<String, String> derivedDeploymentParameters;
    private EntandoControllerFailure entandoControllerFailure;
    private ResourceReference providedCapability;

    protected AbstractServerStatus() {
        //For json deserialization
    }

    protected AbstractServerStatus(String qualifier) {
        this.qualifier = qualifier;
    }

    public void finish() {
        this.finished = new Date();
    }

    public Date getStarted() {
        return started;
    }

    public Date getFinished() {
        return finished;
    }

    public EntandoControllerFailure getEntandoControllerFailure() {
        return entandoControllerFailure;
    }

    public void setEntandoControllerFailure(EntandoControllerFailure entandoControllerFailure) {
        this.entandoControllerFailure = entandoControllerFailure;
    }

    public boolean hasFailed() {
        return entandoControllerFailure != null;
        //TODO incorporate pod status here
        //Requires PodResult class from entando-k8s-operator
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    public Map<String, String> getDerivedDeploymentParameters() {
        return derivedDeploymentParameters;
    }

    public void putDerivedDeploymentParameter(String parameterName, String parameterValue) {
        if (derivedDeploymentParameters == null) {
            derivedDeploymentParameters = new HashMap<>();
        }
        derivedDeploymentParameters.put(parameterName, parameterValue);
    }

    public Map<String, String> getPersistentVolumeClaimPhases() {
        return persistentVolumeClaimPhases;
    }

    public void putPersistentVolumeClaimPhase(String pvcName, String pvcPhase) {
        if (persistentVolumeClaimPhases == null) {
            persistentVolumeClaimPhases = new HashMap<>();
        }
        persistentVolumeClaimPhases.put(pvcName, pvcPhase);
    }

    public Map<String, String> getPodPhases() {
        return podPhases;
    }

    public void putPodPhase(String podName, String podPhase) {
        if (podPhases == null) {
            podPhases = new HashMap<>();
        }
        podPhases.put(podName, podPhase);
    }

    public void finishWith(EntandoControllerFailure failure) {
        finish();
        setEntandoControllerFailure(failure);
    }

    public Optional<String> getAdminSecretName() {
        return Optional.ofNullable(adminSecretName);
    }

    public void setAdminSecretName(String adminSecretName) {
        this.adminSecretName = adminSecretName;
    }

    public ResourceReference getProvidedCapability() {
        return providedCapability;
    }

    public void setProvidedCapability(ResourceReference providedCapability) {
        this.providedCapability = providedCapability;
    }
}
