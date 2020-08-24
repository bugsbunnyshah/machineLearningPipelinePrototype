/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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


/**
 *
 * @author babyboy
 */
@QuarkusTest
public class AWSAIClientTests {
    private static Logger logger = LoggerFactory.getLogger(AWSAIClientTests.class);
    
    @Test
    public void testComprehend() throws Exception
    {
        /*String message = "I_USED_TO_THINK_I_AM_GOD_BUT_I_KNOW_NOW_I_DONT_BELONG_HERE_I_AM_NOT_HIP_ENOUGH_FOR_APP_GAL_AKA_MOTHER_EARTH";
        String json = "{message=\"I_USED_TO_THINK_I_AM_GOD_BUT_I_KNOW_NOW_I_DONT_BELONG_HERE_I_AM_NOT_HIP_ENOUGH_FOR_APP_GAL_AKA_MOTHER_EARTH\"}";
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        while(true)
        {
            logger.info("****************");
            //logger.info("I_USED_TO_THINK_I_AM_GOD_BUT_I_KNOW_NOW_I_DONT_BELONG_HERE_I_AM_NOT_HIP_ENOUGH_FOR_APP_GAL_AKA_MOTHER_EARTH");
            logger.info(jsonObject.toString());
            logger.info("****************");
        }*/
        HttpClient httpClient = HttpClient.newBuilder().build();
        try
        {
            String searchQuery = "{string}";
            searchQuery = URLEncoder.encode(searchQuery,"UTF-8");
            String requestUrl = "https://appgalentitysearch.cognitiveservices.azure.com/bing/v7.0/entities/?mkt=en-us&count=10&offset=0&safesearch=Moderate&q="+searchQuery;

            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
            HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Ocp-Apim-Subscription-Key","42f903592a524b86949d9324e02a99ce")
                    //.param("q", "{string}")
                    //.setParameter("mkt", "en-us")
                    //.setParameter("count", "10")
                    //.setParameter("offset", "0")
                    //.setParameter("safesearch", "Moderate")
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            logger.info(httpResponse.body());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
