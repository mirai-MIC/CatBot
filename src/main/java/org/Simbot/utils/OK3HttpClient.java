package org.Simbot.utils;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author mirai
 * @version 1.0
 * @className OK3HttpClient
 * @data 2023/01/20 21:48
 * @description 发起http请求
 */

public class OK3HttpClient {
    private static final Logger log = LoggerFactory.getLogger(OK3HttpClient.class);

    /**
     * 发起get请求
     *
     * @param url
     * @param params
     * @param headMap
     * @return json数据
     */
    public static String httpGet(String url, Map<String, Object> params, Map<String, String> headMap) {
        // 设置HTTP请求参数
        String result = null;
        url += getParams(params);
        var setHeaders = SetHeaders(headMap);
        var okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);
        var request = builder.build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            result = response.body().string();
        } catch (Exception e) {
            log.error("调用三方接口出错", e);
        }
        return result;
    }

    public static void httpGetAsync(String url, Map<String, Object> params, Map<String, String> headMap, Consumer<String> onSuccess, Consumer<Exception> onError) {
        url += getParams(params);
        var setHeaders = SetHeaders(headMap);
        var okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);
        var request = builder.build();
        var call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                onSuccess.accept(result);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onError.accept(e);
            }
        });
    }


    /**
     * 请求参数
     *
     * @return params
     */
    private static String getParams(Map<String, Object> params) {
        var sb = new StringBuilder("?");
        if (params != null) {
            params.forEach((key, value) -> {
                if (value != null) {
                    sb.append("&");
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
            });
            return sb.toString();
        } else return "";
    }

    /**
     * 请求头
     *
     * @param headersParams
     * @return
     */
    private static Headers SetHeaders(Map<String, String> headersParams) {
        var headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        return headersbuilder.build();
    }
}