package org.Simbot.utils.HttpClientUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class HttpClientUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    @SneakyThrows
    public static String httpGet(String url, Map<String, String> params, Map<String, String> headers) {

        try {
            AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
            RequestBuilder requestBuilder = new RequestBuilder(HttpConstants.Methods.GET);
            requestBuilder.setUrl(GetQueryParams.appendQueryParams(url, params));

            if (headers != null) {
                HttpHeadersBuilder.create().addHeaders(headers).applyHeaders(requestBuilder);
            }


            Response response = asyncHttpClient.executeRequest(requestBuilder.build()).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return response.getResponseBody();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn(MessageFormat.format("get请求异常{0}", e.getMessage()));
        }
        return null;
    }

    public static String httpPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

        RequestBuilder requestBuilder = new RequestBuilder(HttpConstants.Methods.POST);
        requestBuilder.setUrl(url);
        if (headers != null) {
            HttpHeadersBuilder.create().addHeaders(headers).applyHeaders(requestBuilder);
        }
        if (params != null) {
            ParamsBuilder.create().addParams(params).applyParams(requestBuilder);
        }

        try {
            Response response = asyncHttpClient.executeRequest(requestBuilder.build()).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            log.warn(e.getMessage());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            asyncHttpClient.close();
        }

        return null;
    }
}
