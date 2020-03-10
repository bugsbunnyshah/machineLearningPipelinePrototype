package io.bugsbunny.cloud.kubernetes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class DynamicSpinUpClient {
    private static Logger logger = LoggerFactory.getLogger(DynamicSpinUpClient.class);

    public void invokeKubeAlive()
    {
        logger.info("****INVOKE_KUBE_ALIVE****");
        logger.info("*********************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = "http://127.0.0.1:8080/api";

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

    public void invokeGetPods()
    {
        logger.info("****INVOKE_GET_PODS****");
        logger.info("*********************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = "http://127.0.0.1:8080/api/v1/namespaces/default/pods";

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
