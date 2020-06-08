package org.mitre.harmony.matchers;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mitre.harmony.matchers.matchers.ExactMatcher;
import org.mitre.harmony.matchers.matchers.Matcher;
import org.mitre.harmony.matchers.parameters.MatcherCheckboxParameter;
import org.mitre.harmony.matchers.parameters.MatcherParameter;
import org.mitre.schemastore.model.Schema;
import org.mitre.schemastore.model.SchemaElement;
import org.mitre.schemastore.model.schemaInfo.FilteredSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.HierarchicalSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.SchemaInfo;
import org.mitre.schemastore.model.schemaInfo.model.RelationalSchemaModel;
import org.mitre.schemastore.model.schemaInfo.model.SchemaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class MatchingTests {
    private static Logger logger = LoggerFactory.getLogger(MatchingTests.class);

    @Test
    public void testMatchingScenerioSimple() throws Exception
    {
        try
        {
            Schema schema2 = new Schema();
            schema2.setName("blah");

            SchemaModel schemaModel2 = new RelationalSchemaModel();
            schemaModel2.setName("blahModel2");
            SchemaInfo schemaInfo2 = new SchemaInfo(schema2, new ArrayList<>(), new ArrayList<>());
            HierarchicalSchemaInfo info2 = new HierarchicalSchemaInfo(schemaInfo2);
            info2.setModel(schemaModel2);
            for (int i = 0; i < 10; i++) {
                if (i == 0 || i == 1) {
                    SchemaElement element = new SchemaElement();
                    element.setId(i);
                    element.setName("name" + i);
                    element.setDescription("description" + i);
                    element.setBase(i);
                    info2.getModel().getRootElements(info2).add(element);
                } else {
                    SchemaElement element = new SchemaElement();
                    element.setId(i);
                    element.setName("name" + i);
                    element.setDescription("description" + i);
                    element.setBase(i);
                    info2.getModel().getChildElements(info2, i).add(element);
                }
            }
            FilteredSchemaInfo f2 = new FilteredSchemaInfo(info2);

            Schema schema1 = new Schema();
            schema1.setName("blah2");

            SchemaModel schemaModel1 = new RelationalSchemaModel();
            schemaModel1.setName("blahModel2");
            SchemaInfo schemaInfo1 = new SchemaInfo(schema1, new ArrayList<>(), new ArrayList<>());
            HierarchicalSchemaInfo info1 = new HierarchicalSchemaInfo(schemaInfo1);
            info1.setModel(schemaModel1);
            for (int i = 0; i < 10; i++) {
                if (i == 0 || i == 1) {
                    SchemaElement element = new SchemaElement();
                    element.setId(i);
                    element.setName("name" + i);
                    element.setDescription("description" + i);
                    element.setBase(i);
                    info1.getModel().getRootElements(info1).add(element);
                } else {
                    SchemaElement element = new SchemaElement();
                    element.setId(i);
                    element.setName("name" + i);
                    element.setDescription("description" + i);
                    element.setBase(i);
                    info1.getModel().getChildElements(info1, i).add(element);
                }
            }
            FilteredSchemaInfo f1 = new FilteredSchemaInfo(info1);

            logger.info("*******");
            logger.info(info1.getModel().getRootElements(info1).size() + "");
            logger.info(info1.getModel().getRootElements(info1).toString());
            logger.info(info1.getModel().getChildElements(info1, 2).size() + "");
            logger.info(info1.getModel().getChildElements(info1, 2).toString());
            logger.info(f1.getFilteredElements().toString());


            logger.info(info2.getModel().getRootElements(info2).size() + "");
            logger.info(info2.getModel().getRootElements(info2).toString());
            logger.info(info2.getModel().getChildElements(info2, 2).size() + "");
            logger.info(info2.getModel().getChildElements(info2, 2).toString());
            logger.info(f2.getFilteredElements().toString());
            logger.info("*******");

            Matcher quickMatcher = MatcherManager.getMatcher("org.mitre.harmony.matchers.matchers.EntityMatcher");
            assertNotNull(quickMatcher);
            quickMatcher.initialize(f1, f2);

            //Setup the MatcherParameters
            /*ArrayList<MatcherParameter> matcherParameters = exactMatcher.getParameters();
            MatcherParameter name = null;
            for(MatcherParameter parameter:matcherParameters)
            {
                logger.info(parameter.getName());
                if(parameter.getName().equalsIgnoreCase("UseName"))
                {
                    name = parameter;
                    break;
                }
            }
            ((MatcherCheckboxParameter)name).setSelected(Boolean.TRUE);*/

            MatcherScores matcherScores = quickMatcher.match();
            logger.info(matcherScores.getScoreCeiling().toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}
