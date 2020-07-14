package io.bugsbunny.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;

import org.bson.Document;
import org.bson.conversions.Bson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MongoDBJsonStore {
    private static Logger logger = LoggerFactory.getLogger(MongoDBJsonStore.class);

    private MongoClient mongoClient;

    @PostConstruct
    public void start()
    {
        this.mongoClient = MongoClients.create();
    }

    @PreDestroy
    public void stop()
    {
        this.mongoClient.close();
    }

    public void storeIngestion(List<JsonObject> jsonObjects)
    {
        MongoDatabase database = mongoClient.getDatabase("machineLearningPipeline");

        MongoCollection<Document> collection = database.getCollection("ingestion");

        for(JsonObject jsonObject:jsonObjects) {
            Document doc = Document.parse(jsonObject.toString());
            collection.insertOne(doc);
        }
    }


    public JsonObject getIngestion(String ingestionId)
    {
        JsonObject ingestion = new JsonObject();

        MongoDatabase database = mongoClient.getDatabase("machineLearningPipeline");

        MongoCollection<Document> collection = database.getCollection("ingestion");

        String queryJson = "{\"ingestionId\":\""+ingestionId+"\"}";
        Bson bson = Document.parse(queryJson);
        FindIterable<Document> iterable = collection.find(bson);
        MongoCursor<Document> cursor = iterable.cursor();
        while(cursor.hasNext())
        {
            Document document = cursor.next();
            String documentJson = document.toJson();
            ingestion = JsonParser.parseString(documentJson).getAsJsonObject();
            return ingestion;
        }
        return ingestion;
    }

    public List<JsonObject> getIngestedDataSet()
    {
        List<JsonObject> ingestedDataSet = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("machineLearningPipeline");

        MongoCollection<Document> collection = database.getCollection("ingestion");

        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.cursor();
        while(cursor.hasNext())
        {
            Document document = cursor.next();
            String documentJson = document.toJson();
            JsonObject ingestion = JsonParser.parseString(documentJson).getAsJsonObject();
            ingestedDataSet.add(ingestion);
        }
        return ingestedDataSet;
    }

    public void storeIngestionImage(JsonObject jsonObject)
    {
        MongoDatabase database = mongoClient.getDatabase("machineLearningPipeline");

        MongoCollection<Document> collection = database.getCollection("ingestionImage");

        Document doc = Document.parse(jsonObject.toString());
        collection.insertOne(doc);
    }

    public List<JsonObject> getIngestionImages()
    {
        List<JsonObject> ingestion = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("machineLearningPipeline");

        MongoCollection<Document> collection = database.getCollection("ingestionImage");

        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.cursor();
        while(cursor.hasNext())
        {
            Document document = cursor.next();
            String documentJson = document.toJson();
            ingestion.add(JsonParser.parseString(documentJson).getAsJsonObject());
        }
        return ingestion;
    }
}
