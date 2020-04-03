package io.bugsbunny.cloud.kubernetes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive() throws Exception
    {
        logger.info("****INVOKE_KUBE_ALIVE****");
        logger.info("*********************");

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
                .POST(HttpRequest.BodyPublishers.ofString(parameters.toString()))
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
    }

    public void invokePostPod() throws Exception {
        logger.info("****INVOKE_POST_POD****");
        logger.info("***********************");

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

    public void tryGoogleCloud() throws Exception
    {
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
