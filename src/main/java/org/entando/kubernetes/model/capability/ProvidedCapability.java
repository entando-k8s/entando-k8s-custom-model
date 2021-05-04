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

package org.entando.kubernetes.model.capability;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Optional;
import org.entando.kubernetes.model.common.EntandoCustomResource;
import org.entando.kubernetes.model.common.EntandoCustomResourceStatus;
import org.entando.kubernetes.model.common.ResourceReference;
import org.entando.kubernetes.model.common.WebServerStatus;

@JsonSerialize
@JsonDeserialize
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(
        fieldVisibility = Visibility.ANY,
        isGetterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE
)
@RegisterForReflection
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Group("entando.org")
@Version("v1")
public class ProvidedCapability extends CustomResource<CapabilityRequirement, EntandoCustomResourceStatus> implements
        EntandoCustomResource {

    public static final String CAPABILITY_LABEL_NAME = "capability";
    public static final String IMPLEMENTATION_LABEL_NAME = "implementation";
    public static final String CAPABILITY_PROVISION_SCOPE_LABEL_NAME = "capabilityProvisionScope";
    public static final String CRD_NAME = "providedcapability.entando.org";

    public ProvidedCapability() {
    }

    public ProvidedCapability(ObjectMeta meta, CapabilityRequirement capabilityRequirement) {
        super.setMetadata(meta);
        this.spec = capabilityRequirement;
    }

    @Override
    protected EntandoCustomResourceStatus initStatus() {
        return new EntandoCustomResourceStatus();
    }

    public Optional<ResourceReference> getIngressReference() {
        return getStatus().findCurrentServerStatus()
                .filter(WebServerStatus.class::isInstance)
                .flatMap(s -> Optional.of(new ResourceReference(getMetadata().getNamespace(), ((WebServerStatus) s).getIngressName())));
    }

    public ResourceReference getServiceReference() {
        return getStatus().findCurrentServerStatus().map(s -> new ResourceReference(getMetadata().getNamespace(), s.getServiceName()))
                .orElse(null);
    }

    public ResourceReference getAdminSecretReference() {
        return getStatus().findCurrentServerStatus().map(s -> new ResourceReference(getMetadata().getNamespace(), s.getAdminSecretName()))
                .orElse(null);
    }

    @Override
    public String getDefinitionName() {
        return CRD_NAME;
    }
}
