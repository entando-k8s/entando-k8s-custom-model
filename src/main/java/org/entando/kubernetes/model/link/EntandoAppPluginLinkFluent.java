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

import io.fabric8.kubernetes.api.builder.Fluent;
import io.fabric8.kubernetes.api.builder.Nested;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.entando.kubernetes.model.EntandoBaseFluent;

public class EntandoAppPluginLinkFluent<A extends EntandoAppPluginLinkFluent<A>> extends EntandoBaseFluent<A> implements Fluent<A> {

    protected EntandoAppPluginLinkSpecBuilder spec;

    protected EntandoAppPluginLinkFluent() {
        this(new ObjectMetaBuilder(), new EntandoAppPluginLinkSpecBuilder());
    }

    protected EntandoAppPluginLinkFluent(EntandoAppPluginLinkSpec spec, ObjectMeta objectMeta) {
        this(new ObjectMetaBuilder(objectMeta), new EntandoAppPluginLinkSpecBuilder(spec));
    }

    private EntandoAppPluginLinkFluent(ObjectMetaBuilder metadata, EntandoAppPluginLinkSpecBuilder spec) {
        super();
        this.metadata = metadata;
        this.spec = spec;
    }

    public SpecNestedImpl<A> editSpec() {
        return new SpecNestedImpl<>((A) this, this.spec.build());
    }

    public SpecNestedImpl<A> withNewSpec() {
        return new SpecNestedImpl<>((A) this);
    }

    public A withSpec(EntandoAppPluginLinkSpec spec) {
        this.spec = new EntandoAppPluginLinkSpecBuilder(spec);
        return (A) this;
    }

    public static class SpecNestedImpl<N extends EntandoAppPluginLinkFluent> extends
            EntandoAppPluginLinkSpecBuilder<SpecNestedImpl<N>> implements
            Nested<N> {

        private final N parentBuilder;

        SpecNestedImpl(N parentBuilder, EntandoAppPluginLinkSpec item) {
            super(item);
            this.parentBuilder = parentBuilder;
        }

        public SpecNestedImpl(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @Override
        public N and() {
            return (N) parentBuilder.withSpec(this.build());
        }

        public N endSpec() {
            return this.and();
        }
    }

}