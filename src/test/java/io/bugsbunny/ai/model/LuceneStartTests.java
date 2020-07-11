package io.bugsbunny.ai.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

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
import org.apache.lucene.store.*;
import org.apache.lucene.util.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class LuceneStartTests {
    private static Logger logger = LoggerFactory.getLogger(LuceneStartTests.class);

    private List<Document> documentList = new ArrayList<>();

    //@Test
    public void testHelloLucene() throws Exception
    {
        /*StandardAnalyzer analyzer = new StandardAnalyzer();
        this.index();
        Document document = this.documentList.get(0);

        Path indexPath = Files.createTempDirectory("tmp");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));

        indexWriter.addDocument(document);

        int indexCount = indexWriter.numRamDocs();

        logger.info(indexWriter.toString());
        logger.info(indexCount+"");

        String[] files = directory.listAll();
        for(String file:files) {
            logger.info(file);
        }

        //See what a search looks like
        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", analyzer);
        Query query = parser.parse("text");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
        }

        indexWriter.commit();
        indexWriter.flush();
        indexWriter.close();*/

        Analyzer analyzer = new StandardAnalyzer();

        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "We are the World. We are the children. We make this a better place, so lets start living for you my kids";
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", analyzer);
        Query query = parser.parse("the");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        logger.info("Number Of Hits: "+hits.length);
        //assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            logger.info("Value: "+hitDoc.get("fieldname"));
            //assertEquals("We are the World. We are the children. We make this a better place, so lets start living for you my kids", hitDoc.get("fieldname"));
        }
        ireader.close();
        directory.close();
        IOUtils.rm(indexPath);
    }

    @Test
    public void testHelloLuceneJson() throws Exception
    {
        /*StandardAnalyzer analyzer = new StandardAnalyzer();
        this.index();
        Document document = this.documentList.get(0);

        Path indexPath = Files.createTempDirectory("tmp");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));

        indexWriter.addDocument(document);

        int indexCount = indexWriter.numRamDocs();

        logger.info(indexWriter.toString());
        logger.info(indexCount+"");

        String[] files = directory.listAll();
        for(String file:files) {
            logger.info(file);
        }

        //See what a search looks like
        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", analyzer);
        Query query = parser.parse("text");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
        }

        indexWriter.commit();
        indexWriter.flush();
        indexWriter.close();*/

        Analyzer analyzer = new StandardAnalyzer();

        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        String text = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("json.json"),
                StandardCharsets.UTF_8);
        String text2 = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("firstDoc.txt"),
                StandardCharsets.UTF_8);
        Document doc = new Document();
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        Document doc2 = new Document();
        doc2.add(new Field("fieldname", text2, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.addDocument(doc2);
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", analyzer);
        //Query query = parser.parse("\"\"");
        Query query = parser.parse("better place");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        logger.info("Number Of Hits: "+hits.length);
        //assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            logger.info("Value: "+hitDoc.get("fieldname"));
            //assertEquals("We are the World. We are the children. We make this a better place, so lets start living for you my kids", hitDoc.get("fieldname"));
        }
        ireader.close();
        directory.close();
        IOUtils.rm(indexPath);
    }

    private void index() throws IOException {
        /*String fileContent = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("firstDoc.txt"),
                StandardCharsets.UTF_8);
        logger.info(fileContent);

        Document doc = new Document();
        doc.add(new TextField("contents", fileContent, Field.Store.YES));
        this.documentList.add(doc);*/
    }
}
