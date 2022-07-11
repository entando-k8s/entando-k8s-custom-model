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

import io.fabric8.kubernetes.api.builder.Nested;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.entando.kubernetes.model.debundle.EntandoDeBundleSpecFluent.TagNested;

public abstract class EntandoDeBundleDetailsFluent<N extends EntandoDeBundleDetailsFluent<N>> {

    private String name;
    private String description;
    private List<EntandoDeBundleBundleGroupBuilder> bundleGroups;
    private Map<String, Object> distTags;
    private List<String> versions;
    private List<String> keywords;
    private String thumbnail;

    protected EntandoDeBundleDetailsFluent() {
        this.bundleGroups = new ArrayList<>();
        this.distTags = new ConcurrentHashMap<>();
        this.versions = new ArrayList<>();
        this.keywords = new ArrayList<>();
    }

    protected EntandoDeBundleDetailsFluent(EntandoDeBundleDetails details) {
        this.name = details.getName();
        this.description = details.getDescription();
        this.bundleGroups = createBundleGroupsBuilders(
                Optional.ofNullable(details.getBundleGroups()).orElse(new ArrayList<>()));
        this.distTags = Optional.ofNullable(details.getDistTags()).orElse(new ConcurrentHashMap<>());
        this.versions = Optional.ofNullable(details.getVersions()).orElse(new ArrayList<>());
        this.keywords = Optional.ofNullable(details.getKeywords()).orElse(new ArrayList<>());
        this.thumbnail = details.getThumbnail();
    }

    public EntandoDeBundleDetails build() {
        return new EntandoDeBundleDetails(name, description,
                this.bundleGroups.stream().map(EntandoDeBundleBundleGroupFluent::build).collect(Collectors.toList()),
                distTags, versions, keywords, thumbnail);
    }

    private List<EntandoDeBundleBundleGroupBuilder> createBundleGroupsBuilders(
            List<EntandoDeBundleBundleGroup> bundleGroups) {
        return new ArrayList<>(
                bundleGroups.stream().map(EntandoDeBundleBundleGroupBuilder::new).collect(Collectors.toList()));
    }


    public N withName(String name) {
        this.name = name;
        return thisAsN();
    }

    public N withDescription(String description) {
        this.description = description;
        return thisAsN();
    }

    public N withVersions(List<String> versions) {
        this.versions = new ArrayList<>(versions);
        return thisAsN();
    }

    public N addNewVersion(String version) {
        this.versions.add(version);
        return thisAsN();
    }

    public N withKeywords(List<String> keywords) {
        this.keywords = new ArrayList<>(keywords);
        return thisAsN();
    }

    public N addNewKeyword(String keyword) {
        this.keywords.add(keyword);
        return thisAsN();
    }

    public N withThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return thisAsN();
    }

    public N withDistTags(Map<String, Object> distTags) {
        this.distTags = new ConcurrentHashMap<>(distTags);
        return thisAsN();
    }

    public N addNewDistTag(String name, Object value) {
        this.distTags.put(name, value);
        return thisAsN();
    }

    public N withBundleGroups(List<EntandoDeBundleBundleGroup> bundleGroups) {
        this.bundleGroups = createBundleGroupsBuilders(bundleGroups);
        return thisAsN();
    }

    public BundleGroupNested<N> addNewBundleGroup() {
        return new BundleGroupNested<>(thisAsN());
    }

    public N addToBundleGroup(EntandoDeBundleBundleGroup bundleGroup) {
        this.bundleGroups.add(new EntandoDeBundleBundleGroupBuilder(bundleGroup));
        return thisAsN();
    }

    @SuppressWarnings("unchecked")
    protected N thisAsN() {
        return (N) this;
    }


    public static class BundleGroupNested<N extends EntandoDeBundleDetailsFluent> extends
            EntandoDeBundleBundleGroupFluent<BundleGroupNested<N>> implements Nested<N> {

        private final N parentBuilder;

        public BundleGroupNested(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @SuppressWarnings("unchecked")
        public N and() {
            return (N) parentBuilder.addToBundleGroup(super.build());
        }

        public N endBundleGroup() {
            return and();
        }
    }
}
