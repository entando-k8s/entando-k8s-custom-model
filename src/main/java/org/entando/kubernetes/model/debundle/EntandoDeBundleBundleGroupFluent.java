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

package org.entando.kubernetes.model.debundle;

public class EntandoDeBundleBundleGroupFluent<N extends EntandoDeBundleBundleGroupFluent<N>> {

    private String name;
    private String organization;

    public EntandoDeBundleBundleGroupFluent(EntandoDeBundleBundleGroup bundleGroup) {
        this.name = bundleGroup.getName();
        this.organization = bundleGroup.getOrganization();
    }

    public EntandoDeBundleBundleGroupFluent() {
    }

    public EntandoDeBundleBundleGroup build() {
        return new EntandoDeBundleBundleGroup(name, organization);
    }

    public N withName(String name) {
        this.name = name;
        return thisAsN();
    }

    public N withOrganization(String organization) {
        this.organization = organization;
        return thisAsN();
    }

    protected N thisAsN() {
        return (N) this;
    }
}
