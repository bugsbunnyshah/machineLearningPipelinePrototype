package io.bugsbunny.dataScience.endpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class DataSetSearchTests {
    private Logger logger = LoggerFactory.getLogger(DataSetSearchTests.class);

    @Test
    public void testSearch() throws Exception
    {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("lucene.json"),
                StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Response response = given().body(jsonObject.toString()).when().post("/search/phrase")
                .andReturn();
        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        JsonArray result = JsonParser.parseString(jsonResponse).getAsJsonArray();
        logger.info("****");
        logger.info(result.toString());
        logger.info("****");
        //assertNotNull(result.get("calculation"));
    }

}
