package org.Simbot.config.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ycvk
 * @description 本地caffeine缓存配置
 * @date 2023/08/01 22:13
 */
@Configuration
public class CaffeineCacheConfig {
    @Bean
    public Cache<String, CacheDataWrapper> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置软引用，当GC回收并堆内容空间不足，会回收缓存
                .softValues()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                // key过期策略
                .expireAfter(new Expiry<Object, CacheDataWrapper>() {
                    //创建缓存设置过期时间，当TimeUnit参数为空时，不设置过期
                    @Override
                    public long expireAfterCreate(@Nonnull final Object o, @Nonnull final CacheDataWrapper cw, final long l) {
                        if (cw.getUnit() != null) {
                            return cw.getUnit().toNanos(cw.getDelay());
                        }
                        return l;
                    }

                    //更新缓存（相同key）时，取新的过期时间设置
                    @Override
                    public long expireAfterUpdate(@Nonnull final Object o, @Nonnull final CacheDataWrapper cw, final long l, final long l1) {
                        if (cw.getUnit() != null) {
                            return cw.getUnit().toNanos(cw.getDelay());
                        }
                        return l;
                    }

                    //读完缓存不能影响过期时间
                    @Override
                    public long expireAfterRead(@Nonnull final Object o, @Nonnull final CacheDataWrapper cw, final long l, final long l1) {
                        return l1;
                    }
                })
                //缓存操作回调函数
                .build();
    }
}

