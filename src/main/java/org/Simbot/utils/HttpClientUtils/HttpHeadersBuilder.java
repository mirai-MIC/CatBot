package org.Simbot.utils.HttpClientUtils;

import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.RequestBuilder;

import java.util.HashMap;
import java.util.Map;

public class HttpHeadersBuilder {
    private final Map<String, String> headers = new HashMap<>();

    public static HttpHeadersBuilder create() {
        return new HttpHeadersBuilder();
    }

    public HttpHeadersBuilder addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public HttpHeadersBuilder addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RequestBuilder applyHeaders(RequestBuilder requestBuilder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.setHeader(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }

    public BoundRequestBuilder applyHeaders(BoundRequestBuilder requestBuilder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.setHeader(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }
}
