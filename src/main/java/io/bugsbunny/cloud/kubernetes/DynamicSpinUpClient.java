package io.bugsbunny.cloud.kubernetes;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive() throws Exception
    {
        logger.info("****INVOKE_KUBE_ALIVE****");
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
        }
    }

    public void invokeGetPods() throws Exception
    {
        logger.info("****INVOKE_GET_PODS****");
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
        }
    }



}
