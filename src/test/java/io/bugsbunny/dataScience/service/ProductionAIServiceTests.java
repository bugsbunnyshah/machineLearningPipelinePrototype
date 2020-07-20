package io.bugsbunny.dataScience.service;

import com.google.gson.JsonObject;
import io.bugsbunny.persistence.MongoDBJsonStore;
import io.bugsbunny.restclient.MLFlowRunClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ProductionAIServiceTests {
    private static Logger logger = LoggerFactory.getLogger(ProductionAIServiceTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    @Inject
    private ProductionAIService productionAIService;

    @Inject
    private MLFlowRunClient mlFlowRunClient;


    @Test
    public void testProcessLiveModelRequest() throws Exception
    {
        Double result = this.productionAIService.processLiveModelRequest(new JsonObject());

        logger.info("*******");
        logger.info(result.toString());
        logger.info("*******");
    }
}
