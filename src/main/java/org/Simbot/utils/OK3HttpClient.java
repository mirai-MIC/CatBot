package org.Simbot.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author mirai
 * @version 1.0
 * @className OK3HttpClient
 * @data 2023/01/20 21:48
 * @description 发起http请求
 */

@Slf4j
public class OK3HttpClient {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) //设置连接超时
            .writeTimeout(10, TimeUnit.SECONDS) //设置写超时
            .readTimeout(30, TimeUnit.SECONDS) //设置读超时
            .retryOnConnectionFailure(true) //设置是否在连接失败后重试
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES)) //设置连接池
            .cache(new Cache(new File("HttpCache"), 1024 * 1024 * 10)) //设置缓存10M
            .build();

    /**
     * 发起get请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headMap 请求头
     * @return json数据
     */
    public static String httpGet(String url, final Map<String, Object> params, final Map<String, String> headMap) {
        String result = null;
        url += getParams(params);
        final var setHeaders = setHeaders(headMap);
        final Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);
        final var request = builder.build();
        final var call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            result = response.body().string();
        } catch (final Exception e) {
            log.error("调用三方接口出错", e);
        }
        return result;
    }

    public static void httpGetAsync(String url, final Map<String, Object> params, final Map<String, String> headMap, final Consumer<String> onSuccess, final Consumer<Exception> onError) {
        url += getParams(params);
        final var setHeaders = setHeaders(headMap);
        final Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);
        final var request = builder.build();
        final var call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) throws IOException {
                final String result = response.body().string();
                if (onSuccess != null) {
                    onSuccess.accept(result);
                }
            }

            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        });
    }

    public static void httpPostAsync(final String url, final Map<String, Object> params, final Map<String, String> headMap, final Consumer<String> onSuccess, final Consumer<Exception> onError, final int retryTimes) {

        httpCallWrapper(url, params, headMap, onSuccess, onError, retryTimes);
    }

    private static void httpCallWrapper(final String url, final Map<String, Object> params, final Map<String, String> headMap, final Consumer<String> onSuccess, final Consumer<Exception> onError, final int retryTimes) {
        final var setHeaders = setHeaders(headMap);
        final Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);

        final String jsonStr = JSONUtil.toJsonStr(params);
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        final RequestBody body = RequestBody.Companion.create(jsonStr, JSON);
        builder.post(body);

        final var request = builder.build();
        final var call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) throws IOException {
                final String result = response.body().string();
                if (onSuccess != null) {
                    onSuccess.accept(result);
                }
            }

            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {
                if (onError != null) {
                    onError.accept(e);
                }

                if (retryTimes > 0) {
                    httpCallWrapper(url, params, headMap, onSuccess, onError, retryTimes - 1);
                }
            }
        });
    }


    /**
     * 请求参数
     *
     * @return params
     */
    private static String getParams(final Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return "";
        }
        final var sb = new StringBuilder("?");
        params.forEach((key, value) -> {
            if (value != null) {
                if (sb.length() > 1) {
                    sb.append("&");
                }
                try {
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
                } catch (final Exception e) {
                    log.error("error when encoding url", e);
                }
            }
        });
        return sb.toString();
    }

    /**
     * 请求头
     *
     * @param headersParams 请求头参数
     * @return 请求头
     */
    private static Headers setHeaders(final Map<String, String> headersParams) {
        final var headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            headersParams.forEach(headersbuilder::add);
        }
        return headersbuilder.build();
    }
}