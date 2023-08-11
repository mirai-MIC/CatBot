package org.Simbot.utils;

import cn.hutool.core.lang.Pair;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.asynchttpclient.netty.ws.NettyWebSocket;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author ：ycvk
 * @description ：异步httpclient工具类
 * @date ：2023/07/30 16:51
 */
@Slf4j
public class AsyncHttpClientUtil {

    static {
        //添加jvm关闭钩子,保证程序退出时关闭连接池
        Runtime.getRuntime().addShutdownHook(new Thread(AsyncHttpClientUtil::shutdown));
    }

    private static final AsyncHttpClient client = asyncHttpClient(buildClientConfig());
    private static EventLoopGroup eventLoopGroup;

    private static final ExecutorService resultHandlerExecutor = new ThreadPoolExecutor(0,//核心线程数 设置为0 保证线程池中没有线程时销毁线程池 保证内存占用最小
            64,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private static DefaultAsyncHttpClientConfig buildClientConfig() {
        final String osName = System.getProperty("os.name");
        log.warn("初始化asyncHttpClient配置，检测到环境为：" + osName);
        if ("Linux".equalsIgnoreCase(osName)) {
            eventLoopGroup = new EpollEventLoopGroup();//linux下使用epoll,边缘触发,性能更高,基于Linux的epoll
        } else if ("Mac OS X".equalsIgnoreCase(osName)) {
            eventLoopGroup = new KQueueEventLoopGroup();//mac下使用kqueue,边缘触发,基于BSD的kqueue
        } else {
            eventLoopGroup = new NioEventLoopGroup();//windows下使用nio,水位触发,基于Java NIO的异步非阻塞实现
        }

        return new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(500)//连接池最大连接数
                .setMaxConnectionsPerHost(100)//单个服务器最大连接数,不设置此值,爬虫会创建大量的连接,导致服务器拒绝服务
                .setUseInsecureTrustManager(true)//是否信任所有ssl链接
                .setEventLoopGroup(eventLoopGroup)//根据当前系统设置eventLoopGroup
                .setConnectTimeout(60 * 1000)//连接超时时间
                .setReadTimeout(60 * 1000)//读取超时时间
                .setPooledConnectionIdleTimeout(30 * 1000)//连接池中连接的空闲时间
                .setConnectionTtl(30 * 1000)//连接存活时间
                .setMaxRequestRetry(3)//最大重试次数
                .setUseNativeTransport(osName.toLowerCase().contains("linux"))//是否用epoll，仅linux系统支持
                .build();
    }

    /**
     * 通过自定义配置构建一个异步httpclient
     *
     * @return async-http-client
     */
    public static AsyncHttpClient getAsyncClient() {
        return asyncHttpClient(buildClientConfig());
    }

    @SneakyThrows
    public static ByteArrayInputStream downloadImage(final String imgUrl) {
        final CompletableFuture<ByteArrayInputStream> png =
                client.prepareGet(imgUrl).execute()
                        .toCompletableFuture()
                        .thenApplyAsync(resp -> {
                            final InputStream in = resp.getResponseBodyAsStream();
                            // 直接从 InputStream 到 BufferedImage
                            final BufferedImage image;
                            try {
                                image = ImageIO.read(in);
                            } catch (final IOException e) {
                                log.error("读取 imgUrl:{} 失败", imgUrl, e);
                                throw new RuntimeException(e);
                            }
                            // 获取图像的宽度和高度
                            final int width = image.getWidth();
                            final int height = image.getHeight();
                            // 随机生成一个像素点的位置
                            final ThreadLocalRandom random = ThreadLocalRandom.current();
                            final int x = random.nextInt(width);
                            final int y = random.nextInt(height);
                            image.setRGB(x, y, Color.RED.getRGB());

                            // 写回 ByteArrayOutputStream
                            try (ByteArrayOutputStream modifiedOut = new ByteArrayOutputStream(width * height * 4)) {
                                ImageIO.write(image, "png", modifiedOut);
                                return new ByteArrayInputStream(modifiedOut.toByteArray());
                            } catch (final IOException e) {
                                log.error("写入 imgUrl:{} 失败", imgUrl, e);
                                throw new RuntimeException(e);
                            }
                        }).exceptionally(throwable -> {
                            log.error("下载 imgUrl:{} 失败", imgUrl, throwable);
                            return null;
                        });
        return png.get(20, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public static Pair<Request, Response> doGet(final String url) {
        return doGet(url, null);
    }

    /**
     * GET阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return 响应数据
     */
    @SneakyThrows
    public static Pair<Request, Response> doGet(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        final Pair<Request, ListenableFuture<Response>> result = doGetInFuture(url, preRequest);
        return Pair.of(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * GET异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doGetWithResultHandler(final String url, final BiConsumer<Request, Response> resultHandler) {
        doGetWithResultHandler(url, null, resultHandler);
    }

    /**
     * GET异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param preRequest    预请求信息，如：参数、头、请求体等
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doGetWithResultHandler(final String url, final Consumer<BoundRequestBuilder> preRequest, final BiConsumer<Request, Response> resultHandler) {
        final Pair<Request, ListenableFuture<Response>> result = doGetInFuture(url, preRequest);
        addListener(resultHandler, result.getKey(), result.getValue());
    }

    /**
     * GET异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url 请求地址
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static ListenableFuture<Response> doGetInFuture(final String url) {
        return doGetInFuture(url, null).getValue();
    }

    /**
     * GET异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doGetInFuture(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }
        final BoundRequestBuilder builder = client.prepareGet(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return Pair.of(builder.build(), builder.execute());
    }

    public static Pair<Request, ListenableFuture<NettyWebSocket>> doWebSocket(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }
        final BoundRequestBuilder builder = client.prepareGet(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        final ListenableFuture<NettyWebSocket> execute = builder.execute(new WebSocketUpgradeHandler.Builder().build());
        return Pair.of(builder.build(), execute);
    }

    /**
     * POST阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    @SneakyThrows
    public static Pair<Request, Response> doPost(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        final Pair<Request, ListenableFuture<Response>> result = doPostInFuture(url, preRequest);
        return Pair.of(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * POST异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param preRequest    预请求信息，如：参数、头、请求体等
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doPostWithResultHandler(final String url, final Consumer<BoundRequestBuilder> preRequest, final BiConsumer<Request, Response> resultHandler) {
        final Pair<Request, ListenableFuture<Response>> result = doPostInFuture(url, preRequest);
        addListener(resultHandler, result.getKey(), result.getValue());
    }

    /**
     * POST异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doPostInFuture(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }
        final BoundRequestBuilder builder = client.preparePost(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return Pair.of(builder.build(), builder.execute());
    }

    /**
     * POST阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return 响应数据
     */
    @SneakyThrows
    public static Pair<Request, Response> doPut(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        final Pair<Request, ListenableFuture<Response>> result = doPutInFuture(url, preRequest);
        return Pair.of(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * POST异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doPutInFuture(final String url, final Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }
        final BoundRequestBuilder builder = client.preparePut(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return Pair.of(builder.build(), builder.execute());
    }

    @SneakyThrows
    private static Response futureGet(final ListenableFuture<Response> future) {
        if (future == null) {
            return null;
        }
        return future.get();
    }

    private static void addListener(final BiConsumer<Request, Response> resultHandler, final Request preRequest, final ListenableFuture<Response> future) {
        if (future == null || resultHandler == null) {
            return;
        }
        future.addListener(() -> {
            try {
                resultHandler.accept(preRequest, future.get());
            } catch (final Exception e) {
                resultHandler.accept(preRequest, null);
                log.error("异步请求响应数据处理失败", e);
            }
        }, resultHandlerExecutor);
    }

    public static void shutdown() {
        try {
            if (!client.isClosed()) {
                client.close();
                log.warn("关闭asyncHttpClient服务client");
            }
            if (!eventLoopGroup.isShutdown()) {
                eventLoopGroup.shutdownGracefully();
                log.warn("关闭asyncHttpClient服务eventLoopGroup");
            }
            if (!resultHandlerExecutor.isShutdown()) {
                resultHandlerExecutor.shutdown();
                log.warn("关闭asyncHttpClient服务threadPool");
            }
        } catch (final IOException e) {
            log.error("关闭asyncHttpClient服务失败", e);
        }
    }

}
