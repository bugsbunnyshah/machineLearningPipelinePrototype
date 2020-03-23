package io.bugsbunny.cloud.kubernetes;

import com.google.gson.JsonObject;
import io.netty.handler.ssl.SslContext;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.asynchttpclient.*;
import org.asynchttpclient.netty.ssl.JsseSslEngineFactory;
import org.asynchttpclient.request.body.multipart.Part;
import org.asynchttpclient.request.body.multipart.StringPart;
import org.asynchttpclient.util.HttpConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.Future;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive() throws Exception
    {
        /*logger.info("****INVOKE_KUBE_ALIVE****");
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
        String restUrl = "https://kubernetes.docker.internal:6443/api";

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

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .addInterceptorFirst(new ContentLengthHeaderRemover())
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate(requestFactory);
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

        try {
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
        }
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

    private static class ContentLengthHeaderRemover implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest httpRequest, org.apache.http.protocol.HttpContext httpContext) throws org.apache.http.HttpException, IOException {
            httpRequest.removeHeaders(HTTP.CONTENT_LEN);
        }
    }
}
