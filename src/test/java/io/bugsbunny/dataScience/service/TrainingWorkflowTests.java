package io.bugsbunny.dataScience.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bugsbunny.persistence.MongoDBJsonStore;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import io.bugsbunny.restclient.MLFlowRunClient;

@QuarkusTest
public class TrainingWorkflowTests {
    private static Logger logger = LoggerFactory.getLogger(TrainingWorkflowTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testStartTraining() throws Exception
    {
        String runId = this.trainingWorkflow.startTraining();

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        logger.info(runJson);
    }

    @Test
    public void testProcessLiveModelRequest() throws Exception
    {
        Double result = this.trainingWorkflow.processLiveModelRequest(new JsonObject());

        logger.info("*******");
        logger.info(result.toString());
        logger.info("*******");
    }
}
