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

package org.entando.kubernetes.model.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import java.util.Optional;
import org.entando.kubernetes.model.EntandoCustomResourceStatus;
import org.entando.kubernetes.model.HasIngress;
import org.entando.kubernetes.model.RequiresKeycloak;
import org.entando.kubernetes.model.app.EntandoBaseCustomResource;

@JsonSerialize
@JsonDeserialize
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE)
public class EntandoClusterInfrastructure extends EntandoBaseCustomResource implements HasIngress, RequiresKeycloak {

    public static final String CRD_NAME = "entandoclusterinfrastructures.entando.org";

    private EntandoClusterInfrastructureSpec spec;

    public EntandoClusterInfrastructure() {
        this(null);
    }

    public EntandoClusterInfrastructure(EntandoClusterInfrastructureSpec spec) {
        this(new ObjectMeta(), spec);
    }

    public EntandoClusterInfrastructure(ObjectMeta metadata, EntandoClusterInfrastructureSpec spec) {
        this(metadata, spec, null);
    }

    public EntandoClusterInfrastructure(ObjectMeta metadata, EntandoClusterInfrastructureSpec spec,
            EntandoCustomResourceStatus status) {
        super(status);
        super.setMetadata(metadata);
        this.spec = spec;
    }

    public EntandoClusterInfrastructureSpec getSpec() {
        return spec;
    }

    public void setSpec(EntandoClusterInfrastructureSpec spec) {
        this.spec = spec;
    }

    @Override
    public Optional<String> getIngressHostName() {
        return getSpec().getIngressHostName();
    }

    @Override
    public Optional<String> getTlsSecretName() {
        return getSpec().getTlsSecretName();
    }

    @Override
    public Optional<String> getKeycloakSecretToUse() {
        return spec.getKeycloakSecretToUse();
    }
}