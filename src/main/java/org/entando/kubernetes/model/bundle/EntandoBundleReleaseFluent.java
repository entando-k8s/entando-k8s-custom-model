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

package org.entando.kubernetes.model.bundle;

import io.fabric8.kubernetes.api.builder.Nested;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.entando.kubernetes.model.EntandoBaseFluent;

public class EntandoBundleReleaseFluent<A extends EntandoBundleReleaseFluent<A>> extends EntandoBaseFluent<A> {

    protected EntandoBundleReleaseSpecBuilder spec;

    protected EntandoBundleReleaseFluent() {
        this(new ObjectMetaBuilder(), new EntandoBundleReleaseSpecBuilder());
    }

    protected EntandoBundleReleaseFluent(EntandoBundleReleaseSpec spec, ObjectMeta objectMeta) {
        this(new ObjectMetaBuilder(objectMeta), new EntandoBundleReleaseSpecBuilder(spec));
    }

    private EntandoBundleReleaseFluent(ObjectMetaBuilder metadata, EntandoBundleReleaseSpecBuilder spec) {
        super(metadata);
        this.spec = spec;
    }

    public ReleaseSpecNestedImpl<A> editSpec() {
        return new ReleaseSpecNestedImpl<>(thisAsA(), this.spec.build());
    }

    public ReleaseSpecNestedImpl<A> withNewSpec() {
        return new ReleaseSpecNestedImpl<>(thisAsA());
    }

    public A withSpec(EntandoBundleReleaseSpec spec) {
        this.spec = new EntandoBundleReleaseSpecBuilder(spec);
        return thisAsA();
    }

    @SuppressWarnings("unchecked")
    protected A thisAsA() {
        return (A) this;
    }

    public static class ReleaseSpecNestedImpl<N extends EntandoBundleReleaseFluent> extends
            EntandoBundleReleaseSpecFluent<ReleaseSpecNestedImpl<N>> implements
            Nested<N> {

        private final N parentBuilder;

        ReleaseSpecNestedImpl(N parentBuilder, EntandoBundleReleaseSpec spec) {
            super(spec);
            this.parentBuilder = parentBuilder;
        }

        public ReleaseSpecNestedImpl(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @Override
        @SuppressWarnings("unchecked")
        public N and() {
            return (N) parentBuilder.withSpec(this.build());
        }

        public N endSpec() {
            return this.and();
        }
    }

}
