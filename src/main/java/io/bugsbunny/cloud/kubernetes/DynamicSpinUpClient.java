package io.bugsbunny.cloud.kubernetes;

import io.kubernetes.client.openapi.models.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.openapi.models.*;

import java.io.FileReader;
import java.io.IOException;

import javax.net.ssl.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive() throws Exception
    {
        //logger.info("****INVOKE_KUBE_ALIVE****");
        //logger.info("*********************");

        StringBuilder parameters = new StringBuilder();
        //parameters.append("grant_type=client_credentials&");
        //parameters.append("client_id=ffe0d3c1-9024-4111-9b9e-80b51a14eecd&");
        //parameters.append("client_secret=QmIZ_yBfakT90AqVBj3XG?gdi_hr6Hp?&client_secret=QmIZ_yBfakT90AqVBj3XG?gdi_hr6Hp?");
        //parameters.append("scope=https%3A%2F%2Fgraph.microsoft.com%2F.default&");

        parameters.append("client_id=ffe0d3c1-9024-4111-9b9e-80b51a14eecd&");
        parameters.append("response_type=id_token&");
        parameters.append("redirect_uri="+
                URLEncoder.encode("http://localhost:8080/registration/profile", StandardCharsets.UTF_8) +"&");
        parameters.append("response_mode=form_post&");
        parameters.append("scope=openid&");
        parameters.append("state=12345&");
        parameters.append("nonce=678910");


        X509TrustManager xtm = new MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder()
                                .sslContext(ctx)
                                .followRedirects(HttpClient.Redirect.ALWAYS)
                                .build();

        //String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/token&"+parameters.toString();
        //String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/discovery/v2.0/keys&"+parameters.toString();
        //String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/authorize?"+parameters.toString();
        //String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/authorize?client_id=6731de76-14a6-49ae-97bc-6eba6914391e&amp;response_type=id_token&amp;redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F&amp;response_mode=form_post&amp;scope=https%3A%2F%2Fgraph.microsoft.com%2F.default&amp;state=12345&amp;nonce=678910";
        String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/v2.0/authorize?"+parameters.toString();
        logger.info("*******");
        logger.info(restUrl);
        logger.info("*******");
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        logger.info("*******");
        logger.info(tokenJson);
        logger.info("*******");

        JsonObject jsonObject = JsonParser.parseString(tokenJson).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();

        //String kubernetesUrl = "https://appgallabscluster-dns-ff0337d8.hcp.eastus.azmk8s.io:443/api/v1/namespaces/default/pods";
        String kubernetesUrl = "https://kubernetes.docker.internal:6443/api/v1/namespaces/default/pods";
        httpRequestBuilder = HttpRequest.newBuilder();
        httpRequest = httpRequestBuilder.uri(new URI(kubernetesUrl))
                      .header("Bearer",accessToken)
                      .GET()
                      .build();
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info("*******");
        logger.info(httpResponse.body());
        logger.info("*******");
    }

    public void invokeGetPods() throws Exception
    {
        ApiClient client = ClientBuilder.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list =
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
    }

    public void invokePostPodUsingClientLibrary() throws IOException,ApiException
    {
        /*ApiClient client = ClientBuilder.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        //Get the docker image
        String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                StandardCharsets.UTF_8);
        V1Pod pod =
                new V1PodBuilder()
                        .withNewMetadata()
                        .withName("apod")
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName("www")
                        .withImage("nginx")
                        .endContainer()
                        .endSpec()
                        .build();

        api.createNamespacedPod("default", pod, null, null, null);

        V1Pod pod2 =
                new V1Pod()
                        .metadata(new V1ObjectMeta().name("anotherpod"))
                        .spec(
                                new V1PodSpec()
                                        .containers(Arrays.asList(
                                                new V1Container().name("www")
                                                        .image("nginx")
                                        )));

        api.createNamespacedPod("default", pod2, null, null, null);

        V1PodList list =
                api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }

        V1Container container = new V1Container();
        container.setImage(dockerImage);
        container.setName("test");*/

        String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                StandardCharsets.UTF_8);

        logger.info("*******");
        logger.info("POD_CREATION_IN_PROGRESS");
        logger.info("*******");
        ApiClient client = ClientBuilder.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1ObjectMeta meta = new V1ObjectMeta();

        meta.name("ms2-pod");
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "ms2-pod");
        meta.labels(labels);
        V1ContainerPort port = new V1ContainerPort();
        port.containerPort(9090);
        V1Container container = new V1Container();
        container.name("ms2-container");

        //container.image(dockerImage);
        container.image("blah");

        container.imagePullPolicy("IfNotPresent");
        container.ports(Arrays.asList(port));

        V1PodSpec spec = new V1PodSpec();
        spec.containers(Arrays.asList(container));
        V1Pod podBody = new V1Pod();
        podBody.apiVersion("v1");
        podBody.kind("Pod");
        podBody.metadata(meta);
        podBody.spec(spec);

        V1Pod pod = api.createNamespacedPod("default", podBody, null, null, null);
        logger.info("*******");
        logger.info(pod.getSpec().toString());
        logger.info("*******");
    }

    public void invokePostPod() throws Exception {
        logger.info("****INVOKE_POST_POD****");
        logger.info("***********************");

        //Get the docker image
        String dockerImage = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("code-with-quarkus-jvm.tar"),
                StandardCharsets.UTF_8);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("kind", "Deployment");
        jsonObject.addProperty("apiVersion", "v1");
        jsonObject.addProperty("body", dockerImage.substring(0, 10));
        String json = jsonObject.toString();

        X509TrustManager xtm = new MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();


        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        StringBuilder parameters = new StringBuilder();
        parameters.append("grant_type=client_credentials&");
        parameters.append("client_id=ffe0d3c1-9024-4111-9b9e-80b51a14eecd&");
        parameters.append("client_secret=QmIZ_yBfakT90AqVBj3XG?gdi_hr6Hp?&");
        parameters.append("scope=https%3A%2F%2Fgraph.microsoft.com%2F.default&");

        String restUrl = "https://login.microsoftonline.com/ce0e9ab0-4f1a-44ef-9e7d-cee03b25b973/oauth2/token";
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                //.header("Bearer", "eyJ0eXAiOiJKV1QiLCJub25jZSI6IklISHE5Q3pPakw0Y1NWVGtwU1dCc2Q3UGdMay1fdDBKVnF6b1ZkZm5Kc1EiLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwiaWF0IjoxNTg1OTM5NzYyLCJuYmYiOjE1ODU5Mzk3NjIsImV4cCI6MTU4NTk0MzY2MiwiYWlvIjoiNDJkZ1lORElFQW5mL2tETC9kWCs5UWM5eEZ2ZUFRQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJhcHBnYWxsYWJzY2x1c3RlclNQLTIwMjAwNDAxMjE1NzE4IiwiYXBwaWQiOiJmZmUwZDNjMS05MDI0LTQxMTEtOWI5ZS04MGI1MWExNGVlY2QiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jZTBlOWFiMC00ZjFhLTQ0ZWYtOWU3ZC1jZWUwM2IyNWI5NzMvIiwib2lkIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwic3ViIjoiMTZlMDgzOTYtNWJkYS00MTcwLTlhOGEtOGQzNWZjMTdjNTYxIiwidGlkIjoiY2UwZTlhYjAtNGYxYS00NGVmLTllN2QtY2VlMDNiMjViOTczIiwidXRpIjoiZ01BenpaOVcyMHFDa2lzaUFWVWNBQSIsInZlciI6IjEuMCIsInhtc190Y2R0IjoxNTg0OTM2MDk4fQ.cErpze6qU9gv_PWS45t94aXPfzQVS44-DjGSoKjbHqWOB7Aoyx6BIvRRifCmLAXYiCvKpwrHmpsL0YyYmSG22GvHZX8npfnGPv-4TfH3Gjr_dTzp_fyAgsvuZUp94J0ombVi4gI6pl_ZyGA4tVQVFIYwHaeSxvj1wF7c3sYdDi7sqIL3JBP5SGyuNNEBdEtjPVrduX7Wcx8ir3ytQssEOkfVSTIPhioHYumRSjt1HmneqsLI10TIyu7B_ynUrn79_KuV0DUgeyJkEZpcjwG7Ex-FLdxemoANpUytXKG_nqNhvoatCjKjV0xRTNM3T3tKIqRKinxAyP0Ef7UuUWQTVg")
                .POST(HttpRequest.BodyPublishers.ofString(parameters.toString()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        logger.info("*******");
        logger.info(tokenJson);
        logger.info("*******");

        JsonObject tokenJsonObject = JsonParser.parseString(tokenJson).getAsJsonObject();
        String accessToken = tokenJsonObject.get("access_token").getAsString();
        logger.info("*******");
        logger.info(accessToken);
        logger.info("*******");

        logger.info("ABOUT_POST_THE_POST: "+json.length()+"_"+dockerImage.length());
        String kubernetesUrl = "https://kubernetes.docker.internal:6443/apis/apps/v1/namespaces/default/deployments";
        //String kubernetesUrl = "https://appgallabscluster-dns-ff0337d8.hcp.eastus.azmk8s.io:443/api/v1/namespaces/default/deployments";
        httpRequestBuilder = HttpRequest.newBuilder();
        httpRequest = httpRequestBuilder.uri(new URI(kubernetesUrl))
                .header("Authorization", "Bearer "+accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        /*CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info("*******");
        logger.info(future.get().body());
        logger.info("*******");*/

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info("*******");
        logger.info(response.body());
        logger.info("*******");

        /*URL url = new URL("https://appgallabscluster-dns-ff0337d8.hcp.eastus.azmk8s.io:443/api/v1/namespaces/default/deployments");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

        httpsURLConnection.setSSLSocketFactory(sf);

        httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpsURLConnection.setRequestProperty("Accept", "application/json");

        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setDoOutput(true);
        //httpsURLConnection.setChunkedStreamingMode(1024);
        httpsURLConnection.setRequestMethod("POST");
        OutputStream outputStream = httpsURLConnection.getOutputStream();
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));

        try {
            String response = IOUtils.toString(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8);
            //String responseMessage = httpsURLConnection.getResponseMessage();
            logger.info("*******");
            logger.info(response);
            logger.info("*******");
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }

        outputStream.close();*/
    }

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
