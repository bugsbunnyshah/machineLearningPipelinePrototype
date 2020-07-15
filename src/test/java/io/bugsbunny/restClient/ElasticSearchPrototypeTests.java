package io.bugsbunny.restClient;


import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import java.nio.charset.StandardCharsets;

@QuarkusTest
public class ElasticSearchPrototypeTests {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchPrototypeTests.class);

    @Test
    public void testCreateIndex() throws Exception
    {
        String jsonDocument = IOUtils.toString(Thread.currentThread().
                        getContextClassLoader().getResourceAsStream("lucene.json"),
        StandardCharsets.UTF_8);

        logger.info("****");
        logger.info(jsonDocument);
        logger.info("****");

        String requestUrl = "http://localhost:9200/bank/_bulk?pretty&refresh";
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonDocument))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String responseBody = httpResponse.body();
        int status = httpResponse.statusCode();
        logger.info("*******");
        logger.info(status+"");
        logger.info(responseBody);
        logger.info("*******");
    }
}
