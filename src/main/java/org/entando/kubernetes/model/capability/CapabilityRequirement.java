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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.entando.kubernetes.model.common.ResourceReference;

@JsonSerialize
@JsonDeserialize
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(
        fieldVisibility = Visibility.ANY,
        isGetterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE
)
public class CapabilityRequirement {

    private StandardCapability capability;
    private StandardCapabilityImplementation implementation;
    private CapabilityScope scope;
    private CapabilityProvisioningStrategy provisioningStrategy;
    private Map<String, String> selector;
    private Map<String, String> capabilityParameters;
    private ResourceReference specifiedCapability;
    private ExternallyProvidedService externallyProvidedService;

    public CapabilityRequirement() {
    }

    @JsonCreator
    public CapabilityRequirement(@JsonProperty("capability") StandardCapability capability,
            @JsonProperty("implementation") StandardCapabilityImplementation implementation,
            @JsonProperty("capabilityRequirementScope") CapabilityScope scope,
            @JsonProperty("provisioningStrategy") CapabilityProvisioningStrategy provisioningStrategy,
            @JsonProperty("labelsToMatch") Map<String, String> labelsToMatch,
            @JsonProperty("capabilityParameters") Map<String, String> capabilityParameters,
            @JsonProperty("specifiedCapability") ResourceReference specifiedCapability,
            @JsonProperty("externallyProvisionedService") ExternallyProvidedService externallyProvidedService) {
        this.capability = capability;
        this.implementation = implementation;
        this.scope = scope;
        this.provisioningStrategy = provisioningStrategy;
        this.selector = labelsToMatch;
        this.capabilityParameters = capabilityParameters;
        this.specifiedCapability = specifiedCapability;
        this.externallyProvidedService = externallyProvidedService;
    }

    public StandardCapability getCapability() {
        return capability;
    }

    public Optional<StandardCapabilityImplementation> getImplementation() {
        return Optional.ofNullable(implementation);
    }

    public Optional<CapabilityScope> getScope() {
        return Optional.ofNullable(scope);
    }

    public Map<String, String> getSelector() {
        return selector;
    }

    public Map<String, String> getCapabilityParameters() {
        return capabilityParameters;
    }

    public Optional<ResourceReference> getSpecifiedCapability() {
        return Optional.ofNullable(specifiedCapability);
    }

    public Optional<CapabilityProvisioningStrategy> getProvisioningStrategy() {
        return Optional.ofNullable(provisioningStrategy);
    }

    public Optional<ExternallyProvidedService> getExternallyProvisionedService() {
        return Optional.ofNullable(externallyProvidedService);
    }

    public Map<String, String> getCapabilityLabels() {
        Map<String, String> result = new HashMap<>();
        result.put(ProvidedCapability.CAPABILITY_LABEL_NAME, capability.getCamelCaseName());
        result.put(ProvidedCapability.IMPLEMENTATION_LABEL_NAME, implementation.getCamelCaseName());
        result.put(ProvidedCapability.CAPABILITY_PROVISION_SCOPE_LABEL_NAME, scope.getCamelCaseName());
        return result;
    }
}
