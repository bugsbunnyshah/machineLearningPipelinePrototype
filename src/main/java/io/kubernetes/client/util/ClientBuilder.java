/*
Copyright 2017 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package io.kubernetes.client.util;

import com.google.common.base.Strings;
import io.kubernetes.client.openapi.ApiClient;

import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import io.kubernetes.client.util.credentials.Authentication;
import io.kubernetes.client.util.credentials.KubeconfigAuthentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A Builder which allows the construction of {@link ApiClient}s in a fluent fashion. */
public class ClientBuilder {
    private static final Logger log = LoggerFactory.getLogger(ClientBuilder.class);

    private String basePath = Config.DEFAULT_FALLBACK_HOST;
    private byte[] caCertBytes = null;
    private boolean verifyingSsl = true;
    private String overridePatchFormat;
    private Authentication authentication;


    public static ClientBuilder cluster() throws IOException {
        final ClientBuilder builder = new ClientBuilder();

        //final String host = System.getenv(ENV_SERVICE_HOST);
        //final String port = System.getenv(ENV_SERVICE_PORT);
        final String host = "localhost";
        final String port = "443";
        builder.setBasePath(host, port);

        /*final String token =
                new String(
                        Files.readAllBytes(Paths.get(SERVICEACCOUNT_TOKEN_PATH)), Charset.defaultCharset());
        builder.setCertificateAuthority(Files.readAllBytes(Paths.get(SERVICEACCOUNT_CA_PATH)));
        builder.setAuthentication(new AccessTokenAuthentication(token));*/

        return builder;
    }

    protected ClientBuilder setBasePath(String host, String port) {
        try {
            Integer iPort = Integer.valueOf(port);
            URI uri = new URI("https", null, host, iPort, null, null, null);
            this.setBasePath(uri.toString());
        } catch (NumberFormatException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public String getBasePath() {
        return basePath;
    }

    public ClientBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public ClientBuilder setAuthentication(final Authentication authentication) {
        this.authentication = authentication;
        return this;
    }

    public ClientBuilder setCertificateAuthority(final byte[] caCertBytes) {
        this.caCertBytes = caCertBytes;
        return this;
    }

    public boolean isVerifyingSsl() {
        return verifyingSsl;
    }

    public ClientBuilder setVerifyingSsl(boolean verifyingSsl) {
        this.verifyingSsl = verifyingSsl;
        return this;
    }

    public String overridePatchFormat() {
        return overridePatchFormat;
    }

    public ClientBuilder setOverridePatchFormat(String patchFormat) {
        this.overridePatchFormat = patchFormat;
        return this;
    }

    public ApiClient build() {
        final ApiClient client = new ApiClient();
        if (!Strings.isNullOrEmpty(overridePatchFormat)) {
            OkHttpClient withInterceptor =
                    client
                            .getHttpClient()
                            .newBuilder()
                            .addInterceptor(
                                    new Interceptor() {
                                        @Override
                                        public Response intercept(Chain chain) throws IOException {
                                            Request request = chain.request();

                                            if ("PATCH".equals(request.method())) {

                                                //TODO: Investigate this hack
                                                /*com.squareup.okhttp.RequestBody body  = null;
                                                Request newRequest =
                                                        request
                                                                .newBuilder()
                                                                .patch(
                                                                        new ProxyContentTypeRequestBody(
                                                                                body, overridePatchFormat))
                                                                .build();
                                                return chain.proceed(newRequest);*/
                                                return null;
                                            }
                                            return chain.proceed(request);
                                        }
                                    })
                            .build();
            client.setHttpClient(withInterceptor);
        }
        return client;
    }
}