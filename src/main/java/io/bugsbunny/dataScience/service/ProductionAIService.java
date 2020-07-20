package io.bugsbunny.dataScience.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bugsbunny.restclient.MLFlowRunClient;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

@ApplicationScoped
public class ProductionAIService
{
    private static Logger logger = LoggerFactory.getLogger(ProductionAIService.class);

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    public ProductionAIService()
    {
    }

    @PostConstruct
    public void start()
    {

    }

    @PreDestroy
    public void stop()
    {

    }

    public Double processLiveModelRequest(JsonObject json)
    {
        try {
            String runId = "815f68f58d194640b0f3f34b9b6794a9";
            String runJson = this.mlFlowRunClient.getRun(runId);
            //logger.info(runJson);

            JsonObject modelJson = JsonParser.parseString(runJson).getAsJsonObject();
            JsonObject data = modelJson.get("run").getAsJsonObject().get("data").getAsJsonObject();
            String value = data.get("tags").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
            JsonArray valueArray = JsonParser.parseString(value).getAsJsonArray();
            String encodedModelString = valueArray.get(0).getAsJsonObject().get("modelSer").getAsString();
            //logger.info(modelStream);

            ObjectInputStream in = null;
            MultiLayerNetwork model;
            try {
                in = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(encodedModelString)));
                model = (MultiLayerNetwork) in.readObject();
            } finally
            {
                if(in != null) {
                    in.close();
                }
            }

            final double v = model.calcL1(true);

            return v;
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
