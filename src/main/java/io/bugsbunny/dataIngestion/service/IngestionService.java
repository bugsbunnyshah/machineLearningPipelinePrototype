package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonObject;
import io.bugsbunny.persistence.MongoDBJsonStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IngestionService {
    private static Logger logger = LoggerFactory.getLogger(IngestionService.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    public void ingestData(String data)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", data);
        this.mongoDBJsonStore.storeIngestionImage(jsonObject);
    }
}
