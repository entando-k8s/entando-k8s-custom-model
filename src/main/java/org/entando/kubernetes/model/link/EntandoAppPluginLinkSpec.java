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

package org.entando.kubernetes.model.link;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.Optional;
import org.entando.kubernetes.model.common.EntandoMultiTenancy;

@JsonSerialize
@JsonDeserialize
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE)
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntandoAppPluginLinkSpec implements Serializable {

    private String entandoAppNamespace;
    private String entandoAppName;
    private String entandoPluginNamespace;
    private String entandoPluginName;
    private String tenantCode;

    public EntandoAppPluginLinkSpec() {
        //Required for JSON deserialization
    }

    @JsonCreator
    public EntandoAppPluginLinkSpec(
            @JsonProperty("entandoAppNamespace") String entandoAppNamespace,
            @JsonProperty("entandoAppName") String entandoAppName,
            @JsonProperty("entandoPluginNamespace") String entandoPluginNamespace,
            @JsonProperty("entandoPluginName") String entandoPluginName,
            @JsonProperty("tenantCode") String tenantCode) {
        this.entandoAppNamespace = entandoAppNamespace;
        this.entandoAppName = entandoAppName;
        this.entandoPluginNamespace = entandoPluginNamespace;
        this.entandoPluginName = entandoPluginName;
        this.tenantCode = tenantCode;
    }

    public String getEntandoAppName() {
        return entandoAppName;
    }

    public Optional<String> getEntandoAppNamespace() {
        return Optional.ofNullable(entandoAppNamespace);
    }

    public String getEntandoPluginName() {
        return entandoPluginName;
    }

    public Optional<String> getEntandoPluginNamespace() {
        return Optional.ofNullable(entandoPluginNamespace);
    }

    public String getTenantCode() {
        return ofNullable(tenantCode).orElse(EntandoMultiTenancy.PRIMARY_TENANT);
    }
}