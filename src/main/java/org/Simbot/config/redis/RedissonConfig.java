package org.Simbot.config.redis;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：ycvk
 * @description : Redisson配置
 * @date : 2023/08/26 13:27
 */
@Configuration
public class RedissonConfig {

    //获取操作系统名称
    private final String osName = System.getProperty("os.name").toLowerCase();

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");

        config.setCodec(new JsonJacksonCodec())
                .setTransportMode(setTransportMode())
                .setEventLoopGroup(setEventLoopGroup());
        return Redisson.create(config);
    }

    private TransportMode setTransportMode() {
        if (osName.contains("mac")) {
            return TransportMode.KQUEUE;//开启kqueue模式 适用于macOS
        } else if (osName.contains("linux")) {
            return TransportMode.EPOLL;//开启epoll模式 适用于linux
        }
        return TransportMode.NIO;//开启NIO模式 适用于windows
    }

    private EventLoopGroup setEventLoopGroup() {
        if (osName.contains("mac")) {
            return new KQueueEventLoopGroup();//开启kqueue模式 适用于macOS
        } else if (osName.contains("linux")) {
            return new EpollEventLoopGroup();//开启epoll模式 适用于linux
        }
        return new NioEventLoopGroup();//开启NIO模式 适用于windows
    }
}