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

package org.entando.kubernetes.model.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.entando.kubernetes.model.EntandoDeploymentSpecBuilder;

public class EntandoPluginSpecFluent<N extends EntandoPluginSpecFluent> extends EntandoDeploymentSpecBuilder<N> {

    protected final List<String> connectionConfigNames;
    protected final List<ExpectedRole> roles;
    protected final List<Permission> permissions;
    protected final Map<String, String> parameters;
    protected String image;
    protected String ingressPath;
    protected String keycloakSecretToUse;
    protected String clusterInfrastructureToUse;
    protected String healthCheckPath;
    protected PluginSecurityLevel securityLevel;

    public EntandoPluginSpecFluent(EntandoPluginSpec spec) {
        super(spec);
        this.clusterInfrastructureToUse = spec.getClusterInfrastructureTouse().orElse(null);
        this.ingressPath = spec.getIngressPath();
        this.healthCheckPath = spec.getHealthCheckPath();
        this.securityLevel = spec.getSecurityLevel().orElse(null);
        this.image = spec.getImage();
        this.permissions = new ArrayList<>(spec.getPermissions());
        this.connectionConfigNames = new ArrayList<>(spec.getConnectionConfigNames());
        this.roles = new ArrayList<>(spec.getRoles());
        this.parameters = new ConcurrentHashMap<>(spec.getParameters());
        this.keycloakSecretToUse = spec.getKeycloakSecretToUse().orElse(null);
    }

    public EntandoPluginSpecFluent() {
        //default constructor required
        connectionConfigNames = new ArrayList<>();
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
        parameters = new ConcurrentHashMap<>();

    }

    public N withClusterInfrastructureToUse(String name) {
        this.clusterInfrastructureToUse = name;
        return (N) this;
    }

    public N withIngressPath(String ingressPath) {
        this.ingressPath = ingressPath;
        return (N) this;
    }

    public N addNewConnectionConfigName(String name) {
        connectionConfigNames.add(name);
        return (N) this;
    }

    public N withImage(String image) {
        this.image = image;
        return (N) this;
    }

    public N withSecurityLevel(PluginSecurityLevel level) {
        this.securityLevel = level;
        return (N) this;
    }

    public N withKeycloakSecretToUse(String name) {
        this.keycloakSecretToUse = name;
        return (N) this;
    }

    /**
     * Legacy method.
     *
     * @deprecated Use {@link #addNewRole(String, String)}
     */
    @Deprecated
    public N withRole(String code, String name) {
        return addNewRole(code, name);
    }

    public N addNewRole(String code, String name) {
        roles.add(new ExpectedRole(code, name));
        return (N) this;
    }

    /**
     * Legacy method.
     *
     * @deprecated Use {@link #addNewPermission(String, String)}
     */
    @Deprecated
    public N withPermission(String clientId, String role) {
        return addNewPermission(clientId, role);
    }

    public N addNewPermission(String clientId, String role) {
        permissions.add(new Permission(clientId, role));
        return (N) this;
    }

    public N addNewParameter(String name, String value) {
        this.parameters.put(name, value);
        return (N) this;
    }

    public EntandoPluginSpec build() {
        return new EntandoPluginSpec(image, dbms, replicas, ingressPath, keycloakSecretToUse,
                healthCheckPath, securityLevel, tlsSecretName, ingressHostName, roles, permissions, parameters, connectionConfigNames,
                clusterInfrastructureToUse);
    }

    public N withHealthCheckPath(String healthCheckPath) {
        this.healthCheckPath = healthCheckPath;
        return (N) this;
    }

    public N withConnectionConfigNames(List<String> strings) {
        this.connectionConfigNames.clear();
        this.connectionConfigNames.addAll(strings);
        return (N) this;
    }

    public N withRoles(List<ExpectedRole> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
        return (N) this;
    }

    public N withPermissions(List<Permission> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
        return (N) this;
    }

    public N withParameters(Map<String, String> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
        return (N) this;
    }
}