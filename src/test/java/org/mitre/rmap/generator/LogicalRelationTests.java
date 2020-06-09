package org.mitre.rmap.generator;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mitre.schemastore.model.Entity;
import org.mitre.schemastore.model.Project;
import org.mitre.schemastore.model.Schema;
import org.mitre.schemastore.model.SchemaElement;
import org.mitre.schemastore.model.schemaInfo.HierarchicalSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.SchemaInfo;
import org.mitre.schemastore.model.schemaInfo.model.RelationalSchemaModel;
import org.mitre.schemastore.model.schemaInfo.model.SchemaModel;
import org.mitre.schemastore.porters.schemaExporters.SQLExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

@QuarkusTest
public class LogicalRelationTests {
    private static Logger logger = LoggerFactory.getLogger(LogicalRelationTests.class);

    @Test
    public void testCreateLogicalRelations() throws Exception
    {
        Schema schema = new Schema();
        schema.setName("blah");
        SchemaModel schemaModel = new RelationalSchemaModel();
        schemaModel.setName("blahModel");
        ArrayList<Integer> parentSchemaIds = new ArrayList<>();
        ArrayList<SchemaElement> schemaElements = new ArrayList<>();
        Entity element = new Entity();
        element.setId(0);
        element.setName("Id");
        element.setDescription("Id");
        element.setBase(0);
        schemaElements.add(element);
        element = new Entity();
        element.setId(1);
        element.setName("Rcvr");
        element.setDescription("Rcvr");
        element.setBase(1);
        schemaElements.add(element);
        SchemaInfo schemaInfo = new SchemaInfo(schema, parentSchemaIds, schemaElements);
        ArrayList<LogicalRelation> relations = LogicalRelation.createLogicalRelations(schemaInfo);


        logger.info("*******");
        logger.info(schemaInfo.toString());
        logger.info(relations.get(0).getEntitySet().toString());
        logger.info(relations.get(1).getEntitySet().toString());
        logger.info("*******");

        //Testing the Dependency Concept
        Project project = new Project();
        project.setId(0);
        project.setName("project");
        Dependency dependency = new Dependency(relations.get(0), relations.get(1), new ArrayList<>());
        Object[] mappings = dependency.generateMapping(project);
        logger.info(mappings[0].toString());

        /*SQLGenerator sqlGenerator = new SQLGenerator();
        ArrayList<Dependency> dependencies = new ArrayList<>();
        dependencies.add(dependency);
        ArrayList<String> result = sqlGenerator.generate(dependencies, "Derby");
        logger.info(result.toString());*/
    }

    private void addElement(HierarchicalSchemaInfo info1, int id, String name, String description)
    {
        SchemaElement element = new SchemaElement();
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        element.setBase(id);
        info1.getModel().getChildElements(info1, id).add(element);
    }
}
