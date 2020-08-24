package io.bugsbunny.dataScience.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.bugsbunny.dataScience.model.PortableAIModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProductionAIService
{
    private static Logger logger = LoggerFactory.getLogger(ProductionAIService.class);

    private PortableAIModel aiModel;

    public ProductionAIService()
    {
    }

    @PostConstruct
    public void start()
    {
        String runId = "815f68f58d194640b0f3f34b9b6794a9";
        String runJson = "{}";
        JsonObject modelJson = JsonParser.parseString(runJson).getAsJsonObject();
        JsonObject data = modelJson.get("run").getAsJsonObject().get("data").getAsJsonObject();
        String value = data.get("tags").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        JsonArray valueArray = JsonParser.parseString(value).getAsJsonArray();
        String encodedModelString = valueArray.get(0).getAsJsonObject().get("modelSer").getAsString();
        this.aiModel = new PortableAIModel();
        this.aiModel.load(encodedModelString);
    }

    @PreDestroy
    public void stop()
    {
        this.aiModel.unload();
    }

    public Double processLiveModelRequest(JsonObject json)
    {
        try
        {
            final double v = this.aiModel.calculate();
            return v;
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
