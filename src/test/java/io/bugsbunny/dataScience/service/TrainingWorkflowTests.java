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

//import io.bugsbunny.restclient.MLFlowRunClient;

@QuarkusTest
public class TrainingWorkflowTests {
    private static Logger logger = LoggerFactory.getLogger(TrainingWorkflowTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;
    /*private static Logger logger = LoggerFactory.getLogger(TrainingWorkflowTests.class);

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
    }*/

    @Test
    public void testLuceneIndexer() throws Exception
    {
        /*String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("lucene.json"),
                StandardCharsets.UTF_8);

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        List<JsonObject> objects = Arrays.asList(new JsonObject[]{jsonObject});

        this.mongoDBJsonStore.storeIngestion(objects);*/

        JsonObject storedJson = this.mongoDBJsonStore.getIngestion("1");
        logger.info("*******");
        logger.info(storedJson.toString());
        logger.info("*******");

        Analyzer analyzer = new StandardAnalyzer();

        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        String text = storedJson.toString();
        String text2 = storedJson.toString();
        Document doc = new Document();
        doc.add(new Field("data", text, TextField.TYPE_STORED));
        Document doc2 = new Document();
        doc2.add(new Field("data", text2, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.addDocument(doc2);
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("data", analyzer);
        //Query query = parser.parse("\"\"");
        Query query = parser.parse("better place");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        logger.info("Number Of Hits: "+hits.length);
        //assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            logger.info("Value: "+hitDoc.get("data"));
            //assertEquals("We are the World. We are the children. We make this a better place, so lets start living for you my kids", hitDoc.get("fieldname"));
        }
        ireader.close();
        directory.close();
        org.apache.lucene.util.IOUtils.rm(indexPath);
    }
}
