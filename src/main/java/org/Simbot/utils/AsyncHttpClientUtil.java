package org.Simbot.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author ：ycvk
 * @description ：异步httpclient工具类
 * @date ：2023/07/30 16:51
 */
@Slf4j
public class AsyncHttpClientUtil {

    private static final AsyncHttpClient client = asyncHttpClient(buildClientConfig());

    private static DefaultAsyncHttpClientConfig buildClientConfig() {
        return new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(200)//连接池最大连接数
                .setMaxConnectionsPerHost(50)//单个服务器最大连接数
                //3.0.0版本以前的写法
                .setConnectTimeout(60 * 1000)//连接超时时间
                .setReadTimeout(60 * 1000)//读取超时时间
                .setPooledConnectionIdleTimeout(30 * 1000)//连接池中连接的空闲时间
                .setConnectionTtl(30 * 1000)//连接存活时间

                //3.0.0版本以后改用Duration的方式, 为什么不用3.0？因为3.0的版本太新了，fix-protocol-version依赖不支持
//            .setConnectTimeout(Duration.ofSeconds(60))//连接超时时间
//            .setRequestTimeout(Duration.ofSeconds(3600))//请求超时时间
//            .setReadTimeout(Duration.ofSeconds(60))//读取超时时间
//            .setPooledConnectionIdleTimeout(Duration.ofSeconds(30))//连接池中连接的空闲时间
//            .setConnectionTtl(Duration.ofSeconds(30))//连接存活时间
                .setKeepAlive(true)//是否开启keep-alive
                .setFollowRedirect(true)//是否开启重定向
                .setMaxRequestRetry(3)//最大重试次数
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

}
