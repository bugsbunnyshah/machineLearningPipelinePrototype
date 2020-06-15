package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mitre.harmony.matchers.ElementPair;
import org.mitre.harmony.matchers.MatcherManager;
import org.mitre.harmony.matchers.MatcherScore;
import org.mitre.harmony.matchers.MatcherScores;
import org.mitre.harmony.matchers.matchers.Matcher;
import org.mitre.schemastore.model.Entity;
import org.mitre.schemastore.model.Schema;
import org.mitre.schemastore.model.SchemaElement;
import org.mitre.schemastore.model.schemaInfo.FilteredSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.HierarchicalSchemaInfo;
import org.mitre.schemastore.model.schemaInfo.SchemaInfo;
import org.mitre.schemastore.model.schemaInfo.model.RelationalSchemaModel;
import org.mitre.schemastore.model.schemaInfo.model.SchemaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
public class MapperService {
    private static Logger logger = LoggerFactory.getLogger(MapperService.class);

    public JsonObject map(String sourceSchema, String destinationSchema, String sourceData)
    {
        try
        {
            JsonObject sourceElement = JsonParser.parseString(sourceData).getAsJsonObject();
            JsonElement firstElement = sourceElement.get(sourceElement.keySet().iterator().next());
            JsonObject sourceJson;
            if(firstElement.isJsonArray())
            {
                sourceJson = firstElement.getAsJsonArray().get(0).getAsJsonObject();
            }
            else
            {
                sourceJson = firstElement.getAsJsonObject();
            }
            sourceData = sourceJson.toString();


            HierarchicalSchemaInfo source = this.createHierachialSchemaInfo("source");
            HierarchicalSchemaInfo destination = this.createHierachialSchemaInfo("destination");

            this.populateHierarchialSchema(source, sourceData);
            this.populateHierarchialSchema(destination, sourceData);

            ArrayList<SchemaElement> sourceElements = source.getElements(Entity.class);
            ArrayList<SchemaElement> destinationElements = destination.getElements(Entity.class);
            FilteredSchemaInfo f1 = new FilteredSchemaInfo(source);
            f1.addElements(sourceElements);
            FilteredSchemaInfo f2 = new FilteredSchemaInfo(destination);
            f2.addElements(destinationElements);
            Map<SchemaElement, Double> scores = this.findMatches(f1, f2, sourceElements);

            JsonObject result = this.performMapping(scores, sourceData);

            return result;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private HierarchicalSchemaInfo createHierachialSchemaInfo(String schemaName)
    {
        Schema schema = new Schema();
        schema.setName(schemaName);

        SchemaModel schemaModel = new RelationalSchemaModel();
        schemaModel.setName(schemaName+"Model");
        SchemaInfo schemaInfo1 = new SchemaInfo(schema, new ArrayList<>(), new ArrayList<>());
        HierarchicalSchemaInfo schemaInfo = new HierarchicalSchemaInfo(schemaInfo1);
        schemaInfo.setModel(schemaModel);

        return schemaInfo;
    }

    private void populateHierarchialSchema(HierarchicalSchemaInfo schemaInfo, String sourceData)
    {
        JsonObject jsonObject = JsonParser.parseString(sourceData).getAsJsonObject();
        logger.info(jsonObject.toString());

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for(Map.Entry<String, JsonElement> entry:entrySet)
        {
            JsonElement jsonElement = entry.getValue();
            if(jsonElement.isJsonArray())
            {
                jsonElement = jsonElement.getAsJsonArray().get(0);
            }

            String field = entry.getKey();
            SchemaElement schemaElement = new SchemaElement();
            schemaElement.setId(field.hashCode());
            schemaElement.setName(field);
            schemaElement.setDescription(field);
            logger.info("FIELD: "+field);

            if(jsonElement.isJsonObject())
            {
                //HierarchicalSchemaInfo local = this.getHierarchialSchema(schemaName,
                //        jsonElement.getAsJsonObject().toString());
                //schemaInfo.getRootElements().addAll(local.getRootElements());
                this.populateHierarchialSchema(schemaInfo, jsonElement.getAsJsonObject().toString());
            }
            else
            {
                Entity element = new Entity();
                element.setId(field.hashCode());
                element.setName(field);
                element.setDescription(field);
                schemaInfo.addElement(element);
                schemaInfo.getModel().getChildElements(schemaInfo, field
                .hashCode()).add(element);
            }
        }
        logger.info("*******BINDAASBHIDDU******");
        logger.info(schemaInfo.getElements(Entity.class).toString());
    }

    private JsonObject performMapping(Map<SchemaElement, Double> scores, String json) throws IOException
    {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonElement firstElement = root.entrySet().iterator().next().getValue();
        JsonObject jsonObject;
        if(firstElement.isJsonArray())
        {
            jsonObject = firstElement.getAsJsonArray().get(0).getAsJsonObject();
        }
        else
        {
            jsonObject = firstElement.getAsJsonObject();
        }
        logger.info("*******BINDAASBHIDDU******");
        logger.info(jsonObject.toString());

        JsonObject result = new JsonObject();
        Set<Map.Entry<SchemaElement, Double>> entrySet = scores.entrySet();
        for(Map.Entry<SchemaElement, Double> entry: entrySet)
        {
            SchemaElement schemaElement = entry.getKey();
            Double score = entry.getValue();
            String field = schemaElement.getName();
            result.add(field, jsonObject.get(field));
        }

        return result;
    }

    private Map<SchemaElement,Double> findMatches(FilteredSchemaInfo f1, FilteredSchemaInfo f2,
                                                  ArrayList<SchemaElement> sourceElements)
    {
        Map<SchemaElement, Double> result = new HashMap<>();
        Matcher matcher = MatcherManager.getMatcher(
                "org.mitre.harmony.matchers.matchers.EditDistanceMatcher");
        matcher.initialize(f1, f2);

        MatcherScores matcherScores = matcher.match();
        Set<ElementPair> elementPairs = matcherScores.getElementPairs();
        for (ElementPair elementPair : elementPairs) {
            MatcherScore matcherScore = matcherScores.getScore(elementPair);
            Double score = 0d;
            if(matcherScore != null) {
                score = matcherScore.getTotalEvidence();
            }
            for(SchemaElement schemaElement: sourceElements)
            {
                if(schemaElement.getId() == elementPair.getSourceElement())
                {
                    result.put(schemaElement, score);
                }
            }
        }
        return result;
    }
}
