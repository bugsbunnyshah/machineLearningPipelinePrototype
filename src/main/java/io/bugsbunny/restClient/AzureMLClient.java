package io.bugsbunny.restClient;

import com.google.gson.*;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.net.URLEncoder;

import javax.enterprise.context.ApplicationScoped;


/**
 *
 * @author babyboy
 */
@ApplicationScoped
public class AzureMLClient
{
    private static Logger logger = LoggerFactory.getLogger(AzureMLClient.class);

    public void search(String query)
    {
        HttpClient httpClient = HttpClient.newBuilder().build();
        try
        {
            query = URLEncoder.encode(query,"UTF-8");
            String requestUrl = "https://appgalentitysearch.cognitiveservices.azure.com/bing/v7.0/entities/?mkt=en-us&count=10&offset=0&safesearch=Moderate&q="+query;

            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
            HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Ocp-Apim-Subscription-Key","42f903592a524b86949d9324e02a99ce")
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            logger.info(httpResponse.body());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}