package org.Simbot.utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.time.Duration;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author ：ycvk
 * @description ：异步httpclient工具类
 * @date ：2023/07/30 16:51
 */
public class AsyncHttpClientUtil {

    //构建一个默认的config 用于构建AsyncHttpClient
    private static final DefaultAsyncHttpClientConfig clientConfig = new DefaultAsyncHttpClientConfig.Builder()
            .setMaxConnections(200)//连接池最大连接数
            .setMaxConnectionsPerHost(100)//单个服务器最大连接数
            .setConnectTimeout(Duration.ofSeconds(30))//连接超时时间
            .setRequestTimeout(Duration.ofSeconds(30))//请求超时时间
            .setReadTimeout(Duration.ofSeconds(30))//读取超时时间
            .setPooledConnectionIdleTimeout(Duration.ofSeconds(30))//连接池中连接的空闲时间
            .setConnectionTtl(Duration.ofSeconds(30))//连接存活时间
            .setKeepAlive(true)//是否开启keep-alive
            .setFollowRedirect(true)//是否开启重定向
            .build();

    /**
     * 构建一个异步httpclient
     *
     * @return 通过 {@link #clientConfig} 配置 {@link AsyncHttpClient} 的实例
     */
    public static AsyncHttpClient getAsyncClient() {
        return asyncHttpClient(clientConfig);
    }
}
