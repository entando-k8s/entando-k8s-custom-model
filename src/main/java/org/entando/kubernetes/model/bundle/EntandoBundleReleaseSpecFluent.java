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

public abstract class EntandoBundleReleaseSpecFluent<N extends EntandoBundleReleaseSpecFluent> {

    protected String title;
    protected String name;
    protected String organization;
    protected String description;
    protected String thumbnail;
    protected String version;
    protected String url;
    protected EntandoBundleFormat format;
    protected String author;
    protected String signature;
    protected String sha256;
    protected String certificate;
    protected String releaseAt;

    public EntandoBundleReleaseSpecFluent(EntandoBundleReleaseSpec spec) {
        this.title = spec.getTitle().orElse(null);
        this.name = spec.getName().orElse(null);
        this.organization = spec.getOrganization().orElse(null);
        this.description = spec.getDescription().orElse(null);
        this.thumbnail = spec.getThumbnail().orElse(null);
        this.version = spec.getVersion().orElse(null);
        this.url = spec.getUrl().orElse(null);
        this.format = spec.getFormat().orElse(EntandoBundleFormat.GIT);
        this.author = spec.getAuthor().orElse(null);
        this.signature = spec.getSignature().orElse(null);
        this.sha256 = spec.getSha256().orElse(null);
        this.certificate = spec.getCertificate().orElse(null);
        this.releaseAt = spec.getReleaseAt().orElse(null);
    }

    public EntandoBundleReleaseSpecFluent() {
        // Default bundle format is GIT
        this.format = EntandoBundleFormat.GIT;
    }


    public final N withName(String name) {
        this.name = name;
        return thisAsN();
    }

    public final N withTitle(String title) {
        this.title = title;
        return thisAsN();
    }

    public final N withOrganization(String organization) {
        this.organization = organization;
        return thisAsN();
    }

    public final N withDescription(String description) {
        this.description = description;
        return thisAsN();
    }

    public final N withThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return thisAsN();
    }

    public final N withVersion(String version) {
        this.version = version;
        return thisAsN();
    }

    public final N withUrl(String url) {
        this.url = url;
        return thisAsN();
    }

    public final N withFormat(String format) {
        this.format = EntandoBundleFormat.fromValue(format);
        return thisAsN();
    }

    public final N withFormat(EntandoBundleFormat format) {
        this.format = format;
        return thisAsN();
    }

    public final N withAuthor(String author) {
        this.author = author;
        return thisAsN();
    }

    public final N withSignature(String signature) {
        this.signature = signature;
        return thisAsN();
    }

    public final N withSha256(String sha256) {
        this.sha256 = sha256;
        return thisAsN();
    }

    public final N withCertificate(String certificate) {
        this.certificate = certificate;
        return thisAsN();
    }

    public final N withReleaseAt(String releaseAt) {
        this.releaseAt = releaseAt;
        return thisAsN();
    }

    public EntandoBundleReleaseSpec build() {
        return new EntandoBundleReleaseSpec(this.name,
                this.title, this.organization, this.description, this.thumbnail, this.version,
                this.url, this.format != null ? this.format.toValue() : null, this.author, this.signature, this.sha256,
                this.certificate, this.releaseAt
        );
    }


    public N thisAsN() {
        return (N) this;
    }

}
