
package com.tencent.weibo.utils;

import android.util.Log;

import com.loopj.android.http.RetryHandler;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * 自定义参数的Httpclient。<br>
 * 提供httpGet，httpPost两种传送消息的方式<br>
 * 提供httpPost上传文件的方式
 */
public class QHttpClient {
    
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    // 日志输出
    private static String TAG = "QHttpClient.class";

    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;

    public QHttpClient() {
        this(maxConnections, DEFAULT_MAX_CONNECTIONS, socketTimeout, socketTimeout);
    }

    public QHttpClient(int maxConnectionPerHost, int maxTotalConnections, int conTimeOutMs,
            int soTimeOutMs) {

        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
                new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        httpClient = new DefaultHttpClient(cm, httpParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            InflatingEntity inflatingEntity = new InflatingEntity(response.getEntity());
                            response.setEntity(inflatingEntity);
                            break;
                        }
                    }
                }
            }
        });

        httpClient.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_MAX_RETRIES));
    }

    /**
     * Get方法传送消息
     * 
     * @param url 连接的URL
     * @param queryString 请求参数串
     * @return 服务器返回的信息
     * @throws Exception
     */
    public String httpGet(String url, String queryString) throws Exception {

        String responseData = null;
        if (queryString != null && !queryString.equals("")) {
            url += "?" + queryString;
        }
        Log.i(TAG, "QHttpClient httpGet [1] url = " + url);

        HttpGet httpGet = new HttpGet(url);
        httpGet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, socketTimeout);
        httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);
        HttpEntity entity = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, httpContext);
            Log.i(TAG, "QHttpClient httpGet [2] StatusLine : " + response.getStatusLine());
            entity = response.getEntity();
            responseData = EntityUtils.toString(entity);
            Log.i(TAG, "QHttpClient httpGet [3] Response = " + responseData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (entity != null) {
                    entity.consumeContent();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpGet.abort();
        }

        return responseData;
    }

    /**
     * Post方法传送消息
     * 
     * @param url 连接的URL
     * @param queryString 请求参数串
     * @return 服务器返回的信息
     * @throws Exception
     */
    public String httpPost(String url, String queryString) throws Exception {
        String responseData = null;
        URI tmpUri = new URI(url);
        URI uri = URIUtils.createURI(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(),
                tmpUri.getPath(),
                queryString, null);
        Log.i(TAG, "QHttpClient httpPost [1] url = " + uri.toURL());

        HttpPost httpPost = new HttpPost(uri);
        httpPost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, socketTimeout);
        httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);
        if (queryString != null && !queryString.equals("")) {
            StringEntity reqEntity = new StringEntity(queryString);
            // 设置类型
            reqEntity.setContentType("application/x-www-form-urlencoded");
            // 设置请求的数据
            httpPost.setEntity(reqEntity);
        }
        HttpEntity entity = null;
        try {
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            Log.i(TAG, "QHttpClient httpPost [2] StatusLine = " + response.getStatusLine());
            entity = response.getEntity();
            responseData = EntityUtils.toString(entity);
            Log.i(TAG, "QHttpClient httpPost [3] responseData = " + responseData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (entity != null) {
                    entity.consumeContent();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpPost.abort();
        }

        return responseData;
    }

    /**
     * Post方法传送文件和消息
     * 
     * @param url 连接的URL
     * @param queryString 请求参数串
     * @param files 上传的文件列表
     * @return 服务器返回的信息
     * @throws Exception
     */
    public String httpPostWithFile(String url, String queryString, List<NameValuePair> files)
            throws Exception {

        String responseData = null;

        URI tmpUri = new URI(url);
        URI uri = URIUtils.createURI(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(),
                tmpUri.getPath(),
                queryString, null);
        Log.i(TAG, "QHttpClient httpPostWithFile [1]  uri = " + uri.toURL());

        MultipartEntity mpEntity = new MultipartEntity();
        HttpPost httpPost = new HttpPost(uri);
        StringBody stringBody;
        FileBody fileBody;
        File targetFile;
        String filePath;
        FormBodyPart fbp;

        List<NameValuePair> queryParamList = QStrOperate.getQueryParamsList(queryString);
        for (NameValuePair queryParam : queryParamList) {
            stringBody = new StringBody(queryParam.getValue(), Charset.forName("UTF-8"));
            fbp = new FormBodyPart(queryParam.getName(), stringBody);
            mpEntity.addPart(fbp);
            // Log.i(TAG,
            // "------- "+queryParam.getName()+" = "+queryParam.getValue());
        }

        for (NameValuePair param : files) {
            filePath = param.getValue();
            targetFile = new File(filePath);
            fileBody = new FileBody(targetFile, "application/octet-stream");
            fbp = new FormBodyPart(param.getName(), fileBody);
            mpEntity.addPart(fbp);

        }

        // Log.i(TAG,
        // "---------- Entity Content Type = "+mpEntity.getContentType());

        httpPost.setEntity(mpEntity);

        HttpEntity entity = null;
        try {
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            Log.i(TAG, "QHttpClient httpPostWithFile [2] StatusLine = " + response.getStatusLine());
            entity = response.getEntity();
            responseData = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (entity != null) {
                    entity.consumeContent();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpPost.abort();
        }
        Log.i(TAG, "QHttpClient httpPostWithFile [3] responseData = " + responseData);
        return responseData;
    }

    /**
     * 断开QHttpClient的连接
     */
    public void shutdownConnection() {
        try {
            httpClient.getConnectionManager().closeExpiredConnections();
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static class InflatingEntity extends HttpEntityWrapper {
        private GZIPInputStream mGZIP;

        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            mGZIP = new GZIPInputStream(wrappedEntity.getContent());
            return mGZIP;
        }

        @Override
        public long getContentLength() {
            return -1;
        }

        @Override
        public void consumeContent() throws IOException {
            super.consumeContent();
            if (mGZIP != null)
                mGZIP.close();
            wrappedEntity.consumeContent();
        }
    }
}
