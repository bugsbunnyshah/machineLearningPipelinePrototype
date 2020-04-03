package io.bugsbunny.cloud.kubernetes;

import com.google.gson.JsonObject;
import io.netty.handler.ssl.SslContext;
import org.apache.commons.httpclient.HttpException;

import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpHeaders;
//import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.asynchttpclient.*;
import org.asynchttpclient.netty.ssl.JsseSslEngineFactory;
import org.asynchttpclient.request.body.multipart.Part;
import org.asynchttpclient.request.body.multipart.StringPart;
import org.asynchttpclient.util.HttpConstants;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive() throws Exception
    {
        logger.info("****INVOKE_KUBE_ALIVE****");
        logger.info("*********************");

        /*TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);*/

        //Setup RestTemplate
        /*RestTemplate restTemplate = new RestTemplate(requestFactory);

        //Authorize
        try
        {
            String url = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/authorize";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", "ffe0d3c1-9024-4111-9b9e-80b51a14eecd");
            parameters.put("response_type", "code");
            parameters.put("redirect_uri", "https://appgallabscluster-dns-1e842305.hcp.eastus.azmk8s.io:443/api");
            parameters.put("scope", URLEncoder.encode("https%3A%2F%2Fgraph.microsoft.com%2F.default", StandardCharsets.UTF_8.name()));
            RequestEntity<Void> get = RequestEntity.get(new URI(url)).build();
            ResponseEntity<String> authResponse = restTemplate.exchange(url, HttpMethod.GET, get, String.class);

            logger.info("*******");
            logger.info(authResponse.getBody());
            logger.info("*******");
        }
        catch(HttpClientErrorException e){
            logger.error(e.getMessage(), e);
            logger.info(e.getResponseBodyAsString());
        }*/


        //Get a Token



        //String restUrl = "https://kubernetes.docker.internal:6443/api";
        //String restUrl = "https://appgallabscluster-dns-1e842305.hcp.eastus.azmk8s.io:443/api";
        /*String restUrl = "https://appgallabscluster-dns-ff0337d8.hcp.eastus.azmk8s.io:443/api";
        try {
            //Setup the GET request
            URI restURI = new URI(restUrl);
            RequestEntity<Void> requestEntity = RequestEntity.get(restURI).
                    header("Bearer","eyJ0eXAiOiJKV1QiLCJub25jZSI6ImJULUNDWGpoSVQ1NXdCWWl6N3FZeEtKUTBhYVJ3bUVZV1hvU1F3OURXMUUiLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwiaWF0IjoxNTg1ODA3Mjk4LCJuYmYiOjE1ODU4MDcyOTgsImV4cCI6MTU4NTgxMTE5OCwiYWlvIjoiNDJkZ1lOaHR4NkN2Y0VETFZPaFFYSU1lby9vQ0FBPT0iLCJhcHBfZGlzcGxheW5hbWUiOiJhcHBnYWxsYWJzY2x1c3RlclNQLTIwMjAwNDAxMjE1NzE4IiwiYXBwaWQiOiJmZmUwZDNjMS05MDI0LTQxMTEtOWI5ZS04MGI1MWExNGVlY2QiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwib2lkIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwic3ViIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwidGlkIjoiY2UwZTlhYjAtNGYxYS00NGVmLTllN2QtY2VlMDNiMjViOTczIiwidXRpIjoiMUJHZ0JGUy1oMGFxQ0NBOXZCZGFBQSIsInZlciI6IjEuMCIsInhtc190Y2R0IjoxNTg0OTM2MDk4fQ.TG7LgRtjwWuEVlehq1UjQ5KLOrl9J5IwcudIuZ7vWJB5lCqRorlnd6Hz46u7QtNDxPLC52MALlc8UxU78CgbQeUGgBETl7_-3Yf3h0ZY42sxWGy-1OdRRHCd2kUQefFJZS2oVHx7ehiwSNhPsN5ulpiliJZjYvVfId6g318vx-RBExuLfH8x64ZQwGwIkysPvau_XP4ILUVgCpWBLj0mfsErFC8B_rx8eImFTQ_JuUnlJwqYNaQxZBwkqRNbr8650uxcvKCs773tPR4FIR3xu3pqkEpnkFykMhqKkw1jUdLYv1hGLN9q7DqCGz6PaSXE33ESXcvAJ1wCCYizG0jTBg").
                    build();
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            HttpStatus status = response.getStatusCode();

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + status.value());
            logger.info("HttpMessage: " + status.getReasonPhrase());
            logger.info("JSON: " + response.getBody());
            logger.info("**************");
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }*/

        StringBuilder parameters = new StringBuilder();
        parameters.append("grant_type=client_credentials&");
        parameters.append("client_id=ffe0d3c1-9024-4111-9b9e-80b51a14eecd&");
        parameters.append("client_secret=QmIZ_yBfakT90AqVBj3XG?gdi_hr6Hp?&");
        parameters.append("scope=https%3A%2F%2Fgraph.microsoft.com%2F.default&");

        X509TrustManager xtm = new MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();
        String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/token";
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                //.header("Bearer", "eyJ0eXAiOiJKV1QiLCJub25jZSI6IklISHE5Q3pPakw0Y1NWVGtwU1dCc2Q3UGdMay1fdDBKVnF6b1ZkZm5Kc1EiLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwiaWF0IjoxNTg1OTM5NzYyLCJuYmYiOjE1ODU5Mzk3NjIsImV4cCI6MTU4NTk0MzY2MiwiYWlvIjoiNDJkZ1lORElFQW5mL2tETC9kWCs5UWM5eEZ2ZUFRQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJhcHBnYWxsYWJzY2x1c3RlclNQLTIwMjAwNDAxMjE1NzE4IiwiYXBwaWQiOiJmZmUwZDNjMS05MDI0LTQxMTEtOWI5ZS04MGI1MWExNGVlY2QiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwib2lkIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwic3ViIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwidGlkIjoiY2UwZTlhYjAtNGYxYS00NGVmLTllN2QtY2VlMDNiMjViOTczIiwidXRpIjoiZ01BenpaOVcyMHFDa2lzaUFWVWNBQSIsInZlciI6IjEuMCIsInhtc190Y2R0IjoxNTg0OTM2MDk4fQ.cErpze6qU9gv_PWS45t94aXPfzQVS44-DjGSoKjbHqWOB7Aoyx6BIvRRifCmLAXYiCvKpwrHmpsL0YyYmSG22GvHZX8npfnGPv-4TfH3Gjr_dTzp_fyAgsvuZUp94J0ombVi4gI6pl_ZyGA4tVQVFIYwHaeSxvj1wF7c3sYdDi7sqIL3JBP5SGyuNNEBdEtjPVrduX7Wcx8ir3ytQssEOkfVSTIPhioHYumRSjt1HmneqsLI10TIyu7B_ynUrn79_KuV0DUgeyJkEZpcjwG7Ex-FLdxemoANpUytXKG_nqNhvoatCjKjV0xRTNM3T3tKIqRKinxAyP0Ef7UuUWQTVg")
                .POST(HttpRequest.BodyPublishers.ofString(parameters.toString())).build();
        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info("*******");
        logger.info(httpResponse.body());
        logger.info("*******");

        /*try
        {
            String url = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/token";
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap();
            map.add("grant_type", "client_credentials");
            map.add("client_id", "ffe0d3c1-9024-4111-9b9e-80b51a14eecd");
            map.add("client_secret", "QmIZ_yBfakT90AqVBj3XG?gdi_hr6Hp?");
            map.add("scope", URLEncoder.encode("https%3A%2F%2Fgraph.microsoft.com%2F.default",StandardCharsets.UTF_8.name()));

            HttpEntity<MultiValueMap<String, String>> requestEntity= new HttpEntity<>(map, headers);
            String response = restTemplate.postForObject(url,requestEntity,String.class);
            //template.postForObject(url, requestEntity,  String.class);
            //ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            //token = response.getAccessToken() + " - " + response.getTokenType();
            ResponseEntity<String> tokenResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            logger.info("*******");
            logger.info(tokenResponse.getBody());
            logger.info("*******");
        }
        catch(HttpClientErrorException e){
            logger.error(e.getMessage(), e);
            logger.info(e.getResponseBodyAsString());
        }*/
    }

    public void invokeGetPods() throws Exception
    {
        /*logger.info("****INVOKE_GET_PODS****");
        logger.info("*********************");

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String restUrl = "https://kubernetes.docker.internal:6443/api/v1/namespaces/default/pods";

        try {
            //Setup the GET request
            URI restURI = new URI(restUrl);
            HttpMethod get = HttpMethod.GET;
            RequestEntity<Void> requestEntity = RequestEntity.get(restURI).build();
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            HttpStatus status = response.getStatusCode();

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + status.value());
            logger.info("HttpMessage: " + status.getReasonPhrase());
            logger.info("JSON: " + response.getBody());
            logger.info("**************");
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }*/
    }

    public void invokePostPod() throws Exception {
        logger.info("****INVOKE_POST_POD****");
        logger.info("***********************");

        //HttpComponentsClientHttpRequestFactory requestFactory =
        //        new HttpComponentsClientHttpRequestFactory();

        //requestFactory.setHttpClient(httpClient);

        //Setup RestTemplate
        //RestTemplate restTemplate = new RestTemplate(requestFactory);
        String restUrl = "https://kubernetes.docker.internal:6443/apis/apps/v1/namespaces/default/deployments";
        /*try {
            URI restURI = new URI(restUrl);
            HttpMethod post = HttpMethod.POST;

            //Get the docker image
            String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                    StandardCharsets.UTF_8);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("kind", "Deployment");
            jsonObject.addProperty("apiVersion", "v1");
            //jsonObject.addProperty("body", dockerImage);
            String json = jsonObject.toString();

            logger.info("*******");
            System.out.println(json);
            logger.info("*******");

            //RequestEntity postEntity = RequestEntity.post(restURI).contentType(MediaType.APPLICATION_JSON).body(json);
            //ResponseEntity<String> response = restTemplate.exchange(postEntity, String.class);

            //HttpStatus status = response.getStatusCode();

            //logger.info("***RESPONSE***");
            //logger.info("HttpStatus: " + status.value());
            //logger.info("HttpMessage: " + status.getReasonPhrase());
            //logger.info("JSON: " + response.getBody());
            //logger.info("**************");
        }
        catch(HttpClientErrorException hce)
        {
            logger.error(hce.getMessage(), hce);

            logger.info("*******");
            logger.info(hce.getResponseBodyAsString());
            logger.info("*******");

            throw new RuntimeException(hce);
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }*/

        /*try {
            HttpPost httpPost = new HttpPost(restUrl);

            //Get the docker image
            String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                    StandardCharsets.UTF_8);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("kind", "Deployment");
            jsonObject.addProperty("apiVersion", "v1");
            jsonObject.addProperty("body", dockerImage);
            String json = jsonObject.toString();

            httpPost.setHeader("Expect", "100-continue");
            httpPost.setHeader("Content-Length", ""+json.getBytes(StandardCharsets.UTF_8).length);

            logger.info("***JSON****");
            //System.out.println(json);
            logger.info("*******");

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpPost);
            httpClient.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }*/
    }

    public void invokeDeployPod()
    {
        /*try {
            String restUrl = "https://kubernetes.docker.internal:6443/apis/apps/v1/namespaces/default/deployments";

            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            final SslEngineFactory ssl = new JsseSslEngineFactory(sslContext);

            DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                    .setConnectTimeout(500).setSslEngineFactory(ssl);
            AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

            //Get the docker image
            String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                    StandardCharsets.UTF_8);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("kind", "Deployment");
            jsonObject.addProperty("apiVersion", "v1");
            String json = jsonObject.toString();

            Request postRequest = new RequestBuilder(HttpConstants.Methods.POST)
                    .setUrl(restUrl).addBodyPart(new StringPart()).
                            addBodyPart(new StringPart("content",dockerImage))
                    .build();

            Future<Response> responseFuture = client.executeRequest(postRequest);
            final Response response = responseFuture.get();

            logger.info("*******");
            logger.info(response.getStatusCode()+"");
            logger.info(response.getStatusText());
            logger.info(response.getResponseBody());
            logger.info("*******");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }*/
    }

    public void tryGoogleCloud() throws Exception
    {
        String requestUrl = "https://container.googleapis.com/v1beta1/appgallabs/aggregated/usableSubnetworks";
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(requestUrl);

            RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            logger.info("*******");
            logger.info(responseEntity.getBody());
            logger.info("*******");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /*private static class ContentLengthHeaderRemover implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest httpRequest, org.apache.http.protocol.HttpContext httpContext) throws org.apache.http.HttpException, IOException {
            httpRequest.removeHeaders(HTTP.CONTENT_LEN);
        }
    }*/

    public class MyTrustManager implements X509TrustManager
    {   MyTrustManager()
        {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
