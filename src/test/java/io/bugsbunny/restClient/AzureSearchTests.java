package io.bugsbunny.restClient;

//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Test;

//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;

//import junit.*;
import io.bugsbunny.cloud.kubernetes.DynamicSpinUpClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

//@QuarkusTest
public class AzureSearchTests {
    private static Logger logger = LoggerFactory.getLogger(AzureSearchTests.class);

    @Test
    public void test() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://alz.search.windows.net/datasources?api-version=2019-05-06";
        String blobContainerName = "alzBlobContainer";
        String accountName = "alzAccount";
        String primaryKey = "CB509E0E7E50948D4CCAC506BD5655F5";
        String json = "{\n" +
                "  \"name\" : \"cog-search-demo-ds\",\n" +
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
        logger.info("*******");
        logger.info(status+"");
        logger.info(tokenJson);
        logger.info("*******");
    }

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
