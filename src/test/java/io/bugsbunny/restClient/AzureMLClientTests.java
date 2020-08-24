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
public class AzureMLClientTests {
    private static Logger logger = LoggerFactory.getLogger(AzureMLClientTests.class);

    @Inject
    private AzureMLClient azureMLClient;
    
    @Test
    public void testSearch() throws Exception
    {
        String query = "{microsoft}";
        this.azureMLClient.search(query);
    }
}
