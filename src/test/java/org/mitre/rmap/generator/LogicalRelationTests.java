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
        Schema schema1 = new Schema();
        schema1.setName("destination");

        SchemaModel schemaModel1 = new RelationalSchemaModel();
        schemaModel1.setName("destinationModel");
        SchemaInfo schemaInfo1 = new SchemaInfo(schema1, new ArrayList<>(), new ArrayList<>());
        HierarchicalSchemaInfo info1 = new HierarchicalSchemaInfo(schemaInfo1);
        info1.setModel(schemaModel1);

        this.addElement(info1, 0, "Id", "Id");
        this.addElement(info1, 1, "Rcvr", "Rcvr");
        this.addElement(info1, 2, "HasSig", "HasSig");
        ArrayList<LogicalRelation> relations = LogicalRelation.createLogicalRelations(info1);


        logger.info("*******");
        logger.info(info1.getModel().getChildElements(info1, 2).size() + "");
        logger.info(info1.getModel().getChildElements(info1, 2).toString());
        logger.info(relations.toString());
        //logger.info(relations.get(1).getEntitySet().toString());
        logger.info("*******");

        //Testing the Dependency Concept
        Project project = new Project();
        project.setId(0);
        project.setName("project");
        Dependency dependency = new Dependency(relations.get(0), relations.get(1), new ArrayList<>());
        Object[] mappings = dependency.generateMapping(project);
        for(Object mapping:mappings) {
            logger.info(mapping.getClass().getName());
            if(mapping instanceof HierarchicalSchemaInfo) {
                HierarchicalSchemaInfo info = (HierarchicalSchemaInfo) mapping;
                logger.info(info.getElements(Entity.class).toString());
            }
        }

        /*SQLGenerator sqlGenerator = new SQLGenerator();
        ArrayList<Dependency> dependencies = new ArrayList<>();
        dependencies.add(dependency);
        ArrayList<String> result = sqlGenerator.generate(dependencies, "Derby");
        logger.info(result.toString());*/
    }

    private void addElement(HierarchicalSchemaInfo info1, int id, String name, String description)
    {
        Entity element = new Entity();
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        element.setBase(id);
        info1.addElement(element);
        info1.getModel().getChildElements(info1, id).add(element);
    }
}
