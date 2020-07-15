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
    }
}
