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

import java.util.Map;
import org.entando.kubernetes.model.common.ResourceReference;

public class CapabilityRequirementFluent<N extends CapabilityRequirementFluent<N>> {

    private StandardCapability capability;
    private StandardCapabilityImplementation implementation;
    private CapabilityScope capabilityRequirementScope;
    private CapabilityProvisioningStrategy provisioningStrategy;
    private Map<String, String> additionalLabelsToMatch;
    private Map<String, String> capabilityParameters;
    private ResourceReference specifiedCapability;
    private ExternallyProvidedService externallyProvidedService;

    public CapabilityRequirementFluent() {

    }

    public CapabilityRequirementFluent(CapabilityRequirement spec) {
        this.capability = spec.getCapability();
        this.implementation = spec.getImplementation().orElse(null);
        this.capabilityRequirementScope = spec.getScope().orElse(null);
        this.provisioningStrategy = spec.getProvisioningStrategy().orElse(null);
        this.additionalLabelsToMatch = spec.getSelector();
        this.capabilityParameters = spec.getCapabilityParameters();
        this.specifiedCapability = spec.getSpecifiedCapability().orElse(null);
        this.externallyProvidedService = spec.getExternallyProvisionedService().orElse(null);
    }

    public N withCapability(StandardCapability capability) {
        this.capability = capability;
        return thisAsN();
    }

    public N withImplementation(StandardCapabilityImplementation implementation) {
        this.implementation = implementation;
        return thisAsN();
    }

    public N withCapabilityRequirementScope(CapabilityScope capabilityRequirementScope) {
        this.capabilityRequirementScope = capabilityRequirementScope;
        return thisAsN();
    }

    public N withAdditionalLabelsToMatch(Map<String, String> additionalLabelsToMatch) {
        this.additionalLabelsToMatch = additionalLabelsToMatch;
        return thisAsN();
    }

    public N withCapabilityParameters(Map<String, String> capabilityParameters) {
        this.capabilityParameters = capabilityParameters;
        return thisAsN();
    }

    public N withSpecifiedCapability(ResourceReference specifiedCapability) {
        this.specifiedCapability = specifiedCapability;
        return thisAsN();
    }

    public N withProvisioningStrategy(CapabilityProvisioningStrategy provisioningStrategy) {
        this.provisioningStrategy = provisioningStrategy;
        return thisAsN();
    }

    public N withExternallyProvisionedService(String host, Integer port, String adminSecretName) {
        this.externallyProvidedService = new ExternallyProvidedService(host, port, adminSecretName);
        return thisAsN();
    }

    @SuppressWarnings("unchecked")
    protected N thisAsN() {
        return (N) this;
    }

    public CapabilityRequirement build() {
        return new CapabilityRequirement(this.capability, this.implementation, this.capabilityRequirementScope, this.provisioningStrategy,
                this.additionalLabelsToMatch, this.capabilityParameters, this.specifiedCapability, this.externallyProvidedService);
    }

}
