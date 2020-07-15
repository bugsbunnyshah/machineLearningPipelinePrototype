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

@QuarkusTest
public class ElasticSearchClientTests {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchClientTests.class);

    @Inject
    private ElasticSearchClient elasticSearchClient;

    @Test
    public void testCreateIndex() throws Exception
    {
        String jsonDocument = IOUtils.toString(Thread.currentThread().
                        getContextClassLoader().getResourceAsStream("json.json"),
        StandardCharsets.UTF_8);
        JsonArray jsonArray = JsonParser.parseString(jsonDocument).getAsJsonArray();
        logger.info(this.elasticSearchClient.updateIndex(jsonArray));
    }
}
