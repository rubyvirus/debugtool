package com.whatistest.httpserver;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Created by rubyvirusqq@gmail.com on 2017-1-24.
 */
public class HttpServerClient implements HttpServerInterface {


    /**
     * @param
     * @throws IOException
     */

    @Override
    public String httpPost(String requestHost, String requestPath, Map<String, String> params) {
        CloseableHttpClient closeableHttpsClient = this.createableHttpClient();
        try {
            // 拼接请求链接与参数
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setHost(requestHost);
            uriBuilder.setPath(requestPath);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }

            URI uri = new URI(uriBuilder.toString());
            HttpPost httpPost = new HttpPost(uri);
            CloseableHttpResponse httpResponse = closeableHttpsClient.execute(httpPost);
            try {
                HttpEntity httpEntity = httpResponse.getEntity();
                httpResponse.getEntity().getContent();
            } finally {
                httpResponse.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (closeableHttpsClient != null) {
                try {
                    closeableHttpsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 返回请求内容
     *
     * @param
     * @param params
     * @return
     * @throws IOException
     */
    @Override
    public String httpGet(String requestHost, String requestPath, Map<String, String> params) {
        String responseBody = "";
        CloseableHttpClient closeableHttpsClient = this.createableHttpClient();
        // 拼接请求链接与参数
        URIBuilder uriBuilder = new URIBuilder();

        try {
            uriBuilder.setPath(requestHost + requestPath);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            CloseableHttpResponse httpResponse = closeableHttpsClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseBody = EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (closeableHttpsClient != null) {
                try {
                    closeableHttpsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseBody;

    }

    /**
     * 创建https client
     *
     * @return
     */
    private CloseableHttpClient createableHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }
}
