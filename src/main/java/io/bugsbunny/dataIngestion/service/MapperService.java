package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mitre.harmony.matchers.ElementPair;
import org.mitre.harmony.matchers.MatcherManager;
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
import java.nio.charset.StandardCharsets;
import java.util.*;

@ApplicationScoped
public class MapperService {
    private static Logger logger = LoggerFactory.getLogger(MapperService.class);

    public JsonObject map(String sourceSchema, String destinationSchema, String sourceData)
    {
        try
        {
            ArrayList<SchemaElement> sourceSchemaElements = this.parseSchemaElements(sourceSchema);
            ArrayList<SchemaElement> destinationSchemaElements = this.parseSchemaElements(destinationSchema);

            HierarchicalSchemaInfo source = this.getHierarchialSchema("source", sourceSchemaElements);
            FilteredSchemaInfo f1 = new FilteredSchemaInfo(source);

            HierarchicalSchemaInfo destination = this.getHierarchialSchema("destination", destinationSchemaElements);
            FilteredSchemaInfo f2 = new FilteredSchemaInfo(destination);

            Map<SchemaElement, Double> scores = this.findMatches(f1, f2, sourceSchemaElements);
            JsonObject result = this.performMapping(scores, sourceData);

            return result;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private HierarchicalSchemaInfo getHierarchialSchema(String schemaName, ArrayList<SchemaElement> schemaElements)
    {
        Schema schema = new Schema();
        schema.setName(schemaName);

        SchemaModel schemaModel = new RelationalSchemaModel();
        schemaModel.setName(schemaName+"Model");
        SchemaInfo schemaInfo1 = new SchemaInfo(schema, new ArrayList<>(), new ArrayList<>());
        HierarchicalSchemaInfo schemaInfo = new HierarchicalSchemaInfo(schemaInfo1);
        schemaInfo.setModel(schemaModel);

        for(SchemaElement schemaElement:schemaElements)
        {
            this.addElement(schemaInfo, schemaElement.getId(), schemaElement.getName(), schemaElement.getDescription());
        }

        return schemaInfo;
    }

    private void addElement(HierarchicalSchemaInfo schemaInfo, int id, String name, String description)
    {
        Entity element = new Entity();
        element.setId(id);
        element.setName(name);
        element.setDescription(description);
        element.setBase(id);
        schemaInfo.addElement(element);
        schemaInfo.getModel().getChildElements(schemaInfo, id).add(element);
    }

    private ArrayList<SchemaElement> parseSchemaElements(String json) throws IOException
    {
        ArrayList<SchemaElement> schemaElements = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for(Map.Entry<String, JsonElement> entry:entrySet)
        {
            String field = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            SchemaElement schemaElement = new SchemaElement();
            schemaElement.setId(field.hashCode());
            schemaElement.setName(field);
            schemaElement.setDescription(field);
            if(jsonElement.isJsonArray())
            {
                continue;
            }
            schemaElements.add(schemaElement);
        }

        return schemaElements;
    }

    private JsonObject performMapping(Map<SchemaElement, Double> scores, String json) throws IOException
    {
        ArrayList<SchemaElement> schemaElements = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

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
                                                  List<SchemaElement> schemaElements)
    {
        Map<SchemaElement, Double> result = new HashMap<>();
        Matcher matcher = MatcherManager.getMatcher(
                "org.mitre.harmony.matchers.matchers.ExactMatcher");
        matcher.initialize(f1, f2);

        /*ArrayList<MatcherParameter> matcherParameters = matcher.getParameters();
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

        MatcherScores matcherScores = matcher.match();
        for(SchemaElement schemaElement:schemaElements) {
            Integer id = schemaElement.getId();
            Double score = matcherScores.getScore(new ElementPair(id, id)).getTotalEvidence();
            result.put(schemaElement, score);
        }
        return result;
    }
}
