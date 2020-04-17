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

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.Optional;

@JsonSerialize
@JsonDeserialize()
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE)
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntandoBundleReleaseSpec implements Serializable {

    private String title;
    private String name;
    private String organization;
    private String description;
    private String thumbnail;
    private String version;
    private String url;
    private EntandoBundleFormat format;
    private String author;
    private String signature;
    private String sha256;
    private String certificate;
    private String releaseAt;

    public EntandoBundleReleaseSpec() {
        super();
    }

    @SuppressWarnings("unchecked")
    public EntandoBundleReleaseSpec(String name,
            String title, String organization, String description, String thumbnail, String version,
            String url, String format, String author, String signature, String sha256,
            String certificate, String releaseAt) {
        this.title = title;
        this.name = name;
        this.organization = organization;
        this.description = description;
        this.thumbnail = thumbnail;
        this.version = version;
        this.url = url;
        this.format = EntandoBundleFormat.fromValue(format);
        this.author = author;
        this.signature = signature;
        this.sha256 = sha256;
        this.certificate = certificate;
        this.releaseAt = releaseAt;
    }

    public Optional<String> getTitle() {
        return ofNullable(title);
    }

    public Optional<String> getName() {
        return ofNullable(name);
    }

    public Optional<String> getOrganization() {
        return ofNullable(organization);
    }

    public Optional<String> getDescription() {
        return ofNullable(description);
    }

    public Optional<String> getThumbnail() {
        return ofNullable(thumbnail);
    }

    public Optional<String> getVersion() {
        return ofNullable(version);
    }

    public Optional<String> getUrl() {
        return ofNullable(url);
    }

    public Optional<EntandoBundleFormat> getFormat() {
        return ofNullable(format);
    }

    public Optional<String> getAuthor() {
        return ofNullable(author);
    }

    public Optional<String> getSignature() {
        return ofNullable(signature);
    }

    public Optional<String> getSha256() {
        return ofNullable(sha256);
    }

    public Optional<String> getCertificate() {
        return ofNullable(certificate);
    }

    public Optional<String> getReleaseAt() {
        return ofNullable(releaseAt);
    }
}
