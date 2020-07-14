package io.bugsbunny.dataScience.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;

@QuarkusTest
public class SearchIndexerDaemonTests {
    private static Logger logger = LoggerFactory.getLogger(SearchIndexerDaemonTests.class);

    @Inject
    private SearchIndexerDaemon searchIndexerDaemon;

    @Test
    public void testStartIndexer() throws Exception
    {
        this.searchIndexerDaemon.start();
        Thread.sleep(10000);

        // Now search the index:
        /*Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Files.createTempDirectory("tmp");
        Directory directory = FSDirectory.open(indexPath);
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("data", analyzer);
        //Query query = parser.parse("\"\"");
        Query query = parser.parse("better place");
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
        logger.info("Number Of Hits: " + hits.length);
        //assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            logger.info("Value: " + hitDoc.get("data"));
        }
        ireader.close();
        directory.close();*/
    }
}
