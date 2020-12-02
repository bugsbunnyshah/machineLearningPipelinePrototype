package io.bugsbunny.dataScience.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@QuarkusTest
public class TensorFlowTests {
    private static Logger logger = LoggerFactory.getLogger(TensorFlowTests.class);

    //@Test
    public void testEndpoint1() throws Exception
    {
        logger.info("*******");
        logger.info("testEndpoint1");
        logger.info("*******");
        String requestUrl = "http://localhost:8501/v1/models/half_plus_two:predict";
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"instances\": [1.0, 2.0, 5.0]}"))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info(httpResponse.body());
    }

    //@Test
    public void testEndpoint2() throws Exception
    {
        logger.info("*******");
        logger.info("testEndpoint2");
        logger.info("*******");
        String requestUrl = "http://localhost:8501/v1/models/half_plus_two";
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        logger.info(httpResponse.body());
    }
}
