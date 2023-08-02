package org.Simbot.utils.HttpClientUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpClientUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;


    /**
     * 发起HTTP GET请求
     *
     * @param url     请求URL
     * @param params  查询参数
     * @param headers 请求头
     * @return 响应内容
     */
    @SneakyThrows
    public static String httpGet(String url, Map<String, String> params, Map<String, String> headers) {
        try (AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient()) {
            RequestBuilder requestBuilder = new RequestBuilder(HttpConstants.Methods.GET).setUrl(GetQueryParams.appendQueryParams(url, params));

            if (headers != null) {
                HttpHeadersBuilder.create().addHeaders(headers).applyHeaders(requestBuilder);
            }
            Response response = asyncHttpClient.executeRequest(requestBuilder.build()).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return response.getResponseBody();
        }
    }

    /**
     * 发起HTTP POST请求
     *
     * @param url     请求URL
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应内容
     */
    @SneakyThrows
    public static String httpPost(String url, Map<String, String> params, Map<String, String> headers) {
        try (AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient()) {
            RequestBuilder requestBuilder = new RequestBuilder(HttpConstants.Methods.POST).setUrl(url);

            if (headers != null) {
                HttpHeadersBuilder.create().addHeaders(headers).applyHeaders(requestBuilder);
            }

            if (params != null) {
                ParamsBuilder.create().addParams(params).applyParams(requestBuilder);
            }

            Response response = asyncHttpClient.executeRequest(requestBuilder.build()).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return response.getResponseBody();
        }
    }
}
