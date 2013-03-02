package org.dandelion.radiot.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
    private static final int HTTP_OK = 200;
    private final DefaultHttpClient client = new DefaultHttpClient();

    @Override
    public String getStringContent(String url) throws IOException {
        HttpEntity entity = executeRequestFor(url);
        return EntityUtils.toString(entity);
    }

    @Override
    public byte[] getByteContent(String fullUrl) throws IOException {
        HttpEntity entity = executeRequestFor(fullUrl);
        return EntityUtils.toByteArray(entity);
    }

    private HttpEntity executeRequestFor(String url) throws IOException {
        HttpResponse response = client.execute(new HttpGet(url));
        if (!isSuccessful(response)) {
            throw new IOException(response.getStatusLine().getReasonPhrase());
        }
        return response.getEntity();
    }

    private boolean isSuccessful(HttpResponse response) {
        return (response.getStatusLine().getStatusCode() == HTTP_OK) &&
                (response.getEntity() != null);
    }
}
