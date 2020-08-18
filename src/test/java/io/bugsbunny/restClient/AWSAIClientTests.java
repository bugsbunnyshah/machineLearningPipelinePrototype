/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.bugsbunny.restClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

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
        logger.info("****************");
        logger.info("I_USED_TO_THINK_I_AM_GOD_BUT_I_KNOW_NOW_I_DONT_BELONG_HERE_I_AM_NOT_HIP_ENOUGH_FOR_APP_GAL_AKA_MOTHER_EARTH");
        logger.info("****************");
    }   
}
