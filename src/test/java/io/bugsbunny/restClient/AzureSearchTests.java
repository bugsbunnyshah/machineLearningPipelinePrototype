package io.bugsbunny.restClient;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@QuarkusTest
public class AzureSearchTests {
    private static Logger logger = LoggerFactory.getLogger(AzureSearchTests.class);

    @Test
    public void testCreateDataSource() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://alz.search.windows.net/datasources?api-version=2019-05-06";
        String blobContainerName = "azlBlobContainer";
        String accountName = "azlAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";
        String json = "{\n" +
                "  \"name\" : \"azlds\",\n" +
                "  \"description\" : \"Demo files to demonstrate cognitive search capabilities.\",\n" +
                "  \"type\" : \"azureblob\",\n" +
                "  \"credentials\" :\n" +
                "  { \"connectionString\" :\n" +
                "    \"DefaultEndpointsProtocol=https;AccountName=alzBlobContainer;AccountKey=alzBlobContainer;\"\n" +
                "  },\n" +
                "  \"container\" : { \"name\" : \"alzContainer\" }\n" +
                "}";

        X509TrustManager xtm = new AzureSearchTests.MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("api-key",primaryKey)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        int status = httpResponse.statusCode();
        System.out.println("*******");
        System.out.println(status+"");
        System.out.println(tokenJson);
        System.out.println("*******");
    }

    /*
    //@Test
    public void testCreateSkillSet() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://alz.search.windows.net/skillsets/azlds?api-version=2019-05-06";
        String blobContainerName = "azlBlobContainer";
        String accountName = "azlAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";
        String json = "{\n" +
                "  \"description\": \"Extract entities, detect language and extract key-phrases\",\n" +
                "  \"skills\":\n" +
                "  [\n" +
                "    {\n" +
                "      \"@odata.type\": \"#Microsoft.Skills.Text.EntityRecognitionSkill\",\n" +
                "      \"categories\": [ \"Person\", \"Organization\", \"Location\" ],\n" +
                "      \"defaultLanguageCode\": \"en\",\n" +
                "      \"inputs\": [\n" +
                "        { \"name\": \"text\", \"source\": \"/document/content\" }\n" +
                "      ],\n" +
                "      \"outputs\": [\n" +
                "        { \"name\": \"persons\", \"targetName\": \"persons\" },\n" +
                "        { \"name\": \"organizations\", \"targetName\": \"organizations\" },\n" +
                "        { \"name\": \"locations\", \"targetName\": \"locations\" }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@odata.type\": \"#Microsoft.Skills.Text.LanguageDetectionSkill\",\n" +
                "      \"inputs\": [\n" +
                "        { \"name\": \"text\", \"source\": \"/document/content\" }\n" +
                "      ],\n" +
                "      \"outputs\": [\n" +
                "        { \"name\": \"languageCode\", \"targetName\": \"languageCode\" }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@odata.type\": \"#Microsoft.Skills.Text.SplitSkill\",\n" +
                "      \"textSplitMode\" : \"pages\",\n" +
                "      \"maximumPageLength\": 4000,\n" +
                "      \"inputs\": [\n" +
                "        { \"name\": \"text\", \"source\": \"/document/content\" },\n" +
                "        { \"name\": \"languageCode\", \"source\": \"/document/languageCode\" }\n" +
                "      ],\n" +
                "      \"outputs\": [\n" +
                "        { \"name\": \"textItems\", \"targetName\": \"pages\" }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@odata.type\": \"#Microsoft.Skills.Text.KeyPhraseExtractionSkill\",\n" +
                "      \"context\": \"/document/pages/*\",\n" +
                "      \"inputs\": [\n" +
                "        { \"name\": \"text\", \"source\": \"/document/pages/*\" },\n" +
                "        { \"name\":\"languageCode\", \"source\": \"/document/languageCode\" }\n" +
                "      ],\n" +
                "      \"outputs\": [\n" +
                "        { \"name\": \"keyPhrases\", \"targetName\": \"keyPhrases\" }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        X509TrustManager xtm = new AzureSearchTests.MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("api-key",primaryKey)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        int status = httpResponse.statusCode();
        logger.info("*******");
        logger.info(status+"");
        logger.info(tokenJson);
        logger.info("*******");
    }

    //@Test
    public void testCreateIndex() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://alz.search.windows.net/indexes/azlindex?api-version=2019-05-06";
        String blobContainerName = "azlBlobContainer";
        String accountName = "azlAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";
        String json = "{\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"name\": \"id\",\n" +
                "      \"type\": \"Edm.String\",\n" +
                "      \"key\": true,\n" +
                "      \"searchable\": true,\n" +
                "      \"filterable\": false,\n" +
                "      \"facetable\": false,\n" +
                "      \"sortable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"metadata_storage_name\",\n" +
                "      \"type\": \"Edm.String\",\n" +
                "      \"searchable\": false,\n" +
                "      \"filterable\": false,\n" +
                "      \"facetable\": false,\n" +
                "      \"sortable\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"content\",\n" +
                "      \"type\": \"Edm.String\",\n" +
                "      \"sortable\": false,\n" +
                "      \"searchable\": true,\n" +
                "      \"filterable\": false,\n" +
                "      \"facetable\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"languageCode\",\n" +
                "      \"type\": \"Edm.String\",\n" +
                "      \"searchable\": true,\n" +
                "      \"filterable\": false,\n" +
                "      \"facetable\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"keyPhrases\",\n" +
                "      \"type\": \"Collection(Edm.String)\",\n" +
                "      \"searchable\": true,\n" +
                "      \"filterable\": false,\n" +
                "      \"facetable\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"persons\",\n" +
                "      \"type\": \"Collection(Edm.String)\",\n" +
                "      \"searchable\": true,\n" +
                "      \"sortable\": false,\n" +
                "      \"filterable\": true,\n" +
                "      \"facetable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"organizations\",\n" +
                "      \"type\": \"Collection(Edm.String)\",\n" +
                "      \"searchable\": true,\n" +
                "      \"sortable\": false,\n" +
                "      \"filterable\": true,\n" +
                "      \"facetable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"locations\",\n" +
                "      \"type\": \"Collection(Edm.String)\",\n" +
                "      \"searchable\": true,\n" +
                "      \"sortable\": false,\n" +
                "      \"filterable\": true,\n" +
                "      \"facetable\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        X509TrustManager xtm = new AzureSearchTests.MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("api-key",primaryKey)
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        int status = httpResponse.statusCode();
        logger.info("*******");
        logger.info(status+"");
        logger.info(tokenJson);
        logger.info("*******");
    }

    //@Test
    public void testCreateIndexer() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://alz.search.windows.net/indexes/azlindexer?api-version=2019-05-06";
        String blobContainerName = "azlBlobContainer";
        String accountName = "azlAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";
        String json = "{\n" +
                "  \"name\":\"azlindexer\",\n" +
                "\"fieldMappings\" : [\n" +
                "    {\n" +
                "      \"sourceFieldName\" : \"metadata_storage_path\",\n" +
                "      \"targetFieldName\" : \"id\",\n" +
                "      \"mappingFunction\" :\n" +
                "      { \"name\" : \"base64Encode\" }\n" +
                "    }\n" +
                "  ]"+
                "}";
        logger.info(json);

        X509TrustManager xtm = new AzureSearchTests.MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("api-key",primaryKey)
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String tokenJson = httpResponse.body();
        int status = httpResponse.statusCode();
        logger.info("*******");
        logger.info(status+"");
        logger.info(tokenJson);
        logger.info("*******");
    }

    //@Test
    public void testSearch() throws NoSuchAlgorithmException, KeyManagementException, IOException, InterruptedException, URISyntaxException {
        String requestUrl = "https://alz.search.windows.net/indexes/azlindex?api-version=2019-05-06&search=*&$count=true&$select=content";
        String blobContainerName = "azlBlobContainer";
        String accountName = "azlAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";

        X509TrustManager xtm = new AzureSearchTests.MyTrustManager();
        TrustManager mytm[] = {xtm};
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null,mytm, null );
        SSLSocketFactory sf = ctx.getSocketFactory();
        HttpClient httpClient = HttpClient.newBuilder().sslContext(ctx).build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        HttpRequest httpRequest = httpRequestBuilder.uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .header("api-key",primaryKey)
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String json = httpResponse.body();
        int status = httpResponse.statusCode();
        logger.info("*******");
        logger.info(status+"");
        logger.info(json);
        logger.info("*******");
    }*/

    public class MyTrustManager implements X509TrustManager
    {   MyTrustManager()
        {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
