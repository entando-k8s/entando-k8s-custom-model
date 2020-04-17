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

package org.entando.kubernetes.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.fabric8.kubernetes.client.CustomResourceList;
import io.fabric8.kubernetes.client.dsl.internal.CustomResourceOperationsImpl;
import org.entando.kubernetes.model.bundle.DoneableEntandoBundleRelease;
import org.entando.kubernetes.model.bundle.EntandoBundleFormat;
import org.entando.kubernetes.model.bundle.EntandoBundleRelease;
import org.entando.kubernetes.model.bundle.EntandoBundleReleaseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractEntandoBundleReleaseTest implements CustomResourceTestUtil {

    public static final String BUNDLE_NAME = "my-bundle";
    public static final String BUNDLE_ORGANIZATION = "entando";
    public static final String BUNDLE_TITLE = "A title for app-builder";
    public static final String BUNDLE_DESCRIPTION = "my-description";
    public static final String BUNDLE_VERSION = "0.0.1";
    public static final String BUNDLE_SHA256 = "AFGAGARG";
    private static final String BUNDLE_SIGNATURE = "AFGAGARG";
    public static final String BUNDLE_URL = "http://github.com/entando-bundles/blueserver";
    public static final String BUNDLE_THUMBNAIL = "Jyt6tAV2CLeDid2LiT34tA";
    public static final String BUNDLE_AUTHOR = "entando-dev@entando.com";
    public static final String BUNDLE_FORMAT = "git";
    public static final String BUNDLE_RELEASE_AT = "2020-04-17T15:30:00Z";

    protected static final String BUNDLE_NAMESPACE = TestConfig.calculateNameSpace("my-namespace");
    private EntandoResourceOperationsRegistry registry;

    @BeforeEach
    public void deleteEntandoDeBundles() {
        this.registry = new EntandoResourceOperationsRegistry(getClient());
        prepareNamespace(entandoDeBundles(), BUNDLE_NAMESPACE);
    }

    @Test
    public void testCreateEntandoDeBundle() {
        //Given
        EntandoBundleRelease entandoBundleRelease = new EntandoBundleReleaseBuilder()
                .withNewMetadata()
                .withName(BUNDLE_NAME)
                .withNamespace(BUNDLE_NAMESPACE)
                .addToLabels("widgets", "true")
                .addToLabels("plugin", "true")
                .endMetadata()
                .withNewSpec()
                .withName(BUNDLE_NAME)
                .withTitle(BUNDLE_TITLE)
                .withOrganization(BUNDLE_ORGANIZATION)
                .withDescription(BUNDLE_DESCRIPTION)
                .withVersion(BUNDLE_VERSION)
                .withThumbnail(BUNDLE_THUMBNAIL)
                .withAuthor(BUNDLE_AUTHOR)
                .withFormat(BUNDLE_FORMAT)
                .withSignature(BUNDLE_SIGNATURE)
                .withSha256(BUNDLE_SHA256)
                .withUrl(BUNDLE_URL)
                .withReleaseAt(BUNDLE_RELEASE_AT)
                .endSpec()
                .build();
        entandoDeBundles().inNamespace(BUNDLE_NAMESPACE).createNew().withMetadata(entandoBundleRelease.getMetadata())
                .withSpec(entandoBundleRelease.getSpec()).done();
        //When
        EntandoBundleRelease actual = entandoDeBundles().inNamespace(BUNDLE_NAMESPACE).withName(BUNDLE_NAME).get();

        //Then
        assertThat(actual.getSpec().getName(), is(BUNDLE_NAME));
        assertThat(actual.getSpec().getOrganization(), is(BUNDLE_ORGANIZATION));
        assertThat(actual.getSpec().getDescription(), is(BUNDLE_DESCRIPTION));
        assertThat(actual.getSpec().getThumbnail(), is(BUNDLE_THUMBNAIL));
        assertThat(actual.getSpec().getTitle(), is(BUNDLE_TITLE));
        assertThat(actual.getSpec().getVersion(), is(BUNDLE_VERSION));
        assertThat(actual.getSpec().getUrl(), is(BUNDLE_URL));
        assertThat(actual.getSpec().getFormat(), is(EntandoBundleFormat.GIT));
        assertThat(actual.getSpec().getAuthor(), is(BUNDLE_AUTHOR));
        assertThat(actual.getSpec().getSha256(), is(BUNDLE_SHA256));
        assertThat(actual.getSpec().getSignature(), is(BUNDLE_SIGNATURE));
        assertThat(actual.getSpec().getCertificate(), is(null));
        assertThat(actual.getSpec().getReleaseAt(), is(BUNDLE_RELEASE_AT));
        assertThat(actual.getMetadata().getName(), is(BUNDLE_NAME));
    }

    @Test
    public void testEditEntandoDeBundle() {
        //Given
        EntandoBundleRelease entandoApp = new EntandoBundleReleaseBuilder()
                .withNewMetadata()
                .withName(BUNDLE_NAME)
                .withNamespace(BUNDLE_NAMESPACE)
                .endMetadata()
                .withNewSpec()
                .withDescription(BUNDLE_DESCRIPTION)
                .withName(BUNDLE_NAME)
                .withThumbnail("H0cFRNTEJt8EZBcL17_iww")
                .withSignature("asdfasdfasdfasdsafsdfs")
                .withSha256("1234123412341234")
                .withUrl("sdfasdfasdfasdfas")
                .withVersion("0.0.2")
                .endSpec()
                .build();
        //When
        //We are not using the mock server here because of a known bug
        EntandoBundleRelease actual = editEntandoDeBundle(entandoApp)
                .editMetadata()
                .endMetadata()
                .editSpec()
                .withDescription(BUNDLE_DESCRIPTION)
                .withName(BUNDLE_NAME)
                .withThumbnail(BUNDLE_THUMBNAIL)
                .withVersion(BUNDLE_VERSION)
                .withSha256(BUNDLE_SHA256)
                .withUrl(BUNDLE_URL)
                .endSpec()
                .withPhase(EntandoDeploymentPhase.STARTED)
                .done();
        //Then
        assertThat(actual.getSpec().getName(), is(BUNDLE_NAME));
        assertThat(actual.getSpec().getDescription(), is(BUNDLE_DESCRIPTION));
        assertThat(actual.getSpec().getThumbnail(), is(BUNDLE_THUMBNAIL));
        assertThat(actual.getSpec().getVersion(), is(BUNDLE_VERSION));
        assertThat(actual.getSpec().getSha256(), is(BUNDLE_SHA256));
        assertThat(actual.getSpec().getUrl(), is(BUNDLE_URL));
        assertThat(actual.getMetadata().getName(), is(BUNDLE_NAME));
    }

    protected DoneableEntandoBundleRelease editEntandoDeBundle(EntandoBundleRelease entandoApp) {
        entandoDeBundles().inNamespace(BUNDLE_NAMESPACE).create(entandoApp);
        return entandoDeBundles().inNamespace(BUNDLE_NAMESPACE).withName(BUNDLE_NAME).edit();
    }

    protected CustomResourceOperationsImpl<EntandoBundleRelease, CustomResourceList<EntandoBundleRelease>,
            DoneableEntandoBundleRelease> entandoDeBundles() {
        return registry.getOperations(EntandoBundleRelease.class);
    }

}
