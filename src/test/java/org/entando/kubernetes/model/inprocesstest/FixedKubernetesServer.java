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

package org.entando.kubernetes.model.inprocesstest;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesAttributesExtractor;
import io.fabric8.kubernetes.client.server.mock.KubernetesCrudDispatcher;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.fabric8.kubernetes.client.server.mock.KubernetesResponseComposer;
import io.fabric8.mockwebserver.Context;
import io.fabric8.mockwebserver.ServerRequest;
import io.fabric8.mockwebserver.ServerResponse;
import io.fabric8.mockwebserver.crud.Attribute;
import io.fabric8.mockwebserver.crud.AttributeSet;
import io.fabric8.mockwebserver.dsl.MockServerExpectation;
import java.util.HashMap;
import java.util.Queue;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.rules.ExternalResource;

public class FixedKubernetesServer extends ExternalResource {

    private KubernetesMockServer mock;
    private NamespacedKubernetesClient client;
    private boolean https;
    // In this mode the mock web server will store, read, update and delete
    // kubernetes resources using an in memory map and will appear as a real api
    // server.
    private boolean crudMode;

    public FixedKubernetesServer() {
        this(true, false);
    }

    public FixedKubernetesServer(boolean https) {
        this(https, false);
    }

    public FixedKubernetesServer(boolean https, boolean crudMode) {
        this.https = https;
        this.crudMode = crudMode;
    }

    public void before() {
        mock = crudMode
                ? new KubernetesMockServer(new Context(), new MockWebServer(), new HashMap<ServerRequest, Queue<ServerResponse>>(),
                new KubernetesCrudDispatcher(new KubernetesAttributesExtractor() {
                    @Override
                    public AttributeSet fromPath(String s) {
                        AttributeSet attributeSet = super.fromPath(s);
                        if (s.endsWith("/entandobundlereleases") || s.contains("/entandobundlereleases/")) {
                            attributeSet = attributeSet.add(new Attribute("kind", "entandobundlerelease"));
                        }
                        return attributeSet;
                    }
                }, new KubernetesResponseComposer()), true)
                : new KubernetesMockServer(https);
        mock.init();
        client = mock.createClient();
    }

    public void after() {
        mock.destroy();
        client.close();
    }

    public NamespacedKubernetesClient getClient() {
        return client;
    }

    public MockServerExpectation expect() {
        return mock.expect();
    }

    @Deprecated
    public <T> void expectAndReturnAsJson(String path, int code, T body) {
        expect().withPath(path).andReturn(code, body).always();
    }

    @Deprecated
    public <T> void expectAndReturnAsJson(String method, String path, int code, T body) {
        expect().withPath(path).andReturn(code, body).always();
    }

    @Deprecated
    public void expectAndReturnAsString(String path, int code, String body) {
        expect().withPath(path).andReturn(code, body).always();
    }

    @Deprecated
    public void expectAndReturnAsString(String method, String path, int code, String body) {
        expect().withPath(path).andReturn(code, body).always();
    }

    public MockWebServer getMockServer() {
        return mock.getServer();
    }

    public RecordedRequest getLastRequest() throws InterruptedException {
        int count = mock.getServer().getRequestCount();
        RecordedRequest request = null;
        while (count-- > 0) {
            request = mock.getServer().takeRequest();
        }
        return request;
    }
}
