package org.mitre.harmony.matchers;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mitre.schemastore.model.Schema;
import org.mitre.schemastore.model.SchemaElement;
import org.mitre.schemastore.model.schemaInfo.HierarchicalSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.SchemaInfo;
import org.mitre.schemastore.model.schemaInfo.model.RelationalSchemaModel;
import org.mitre.schemastore.model.schemaInfo.model.SchemaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@QuarkusTest
public class MatchingTests {
    private static Logger logger = LoggerFactory.getLogger(MatchingTests.class);

    @Test
    public void testMatchingScenerioSimple() throws Exception
    {
        logger.info("*******");
        logger.info(MatcherManager.getMatchers().toString());
        logger.info("*******");


    }
}
