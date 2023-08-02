package org.Simbot.utils;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import org.Simbot.config.caffeine.CacheDataWrapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author ycvk
 * @description 本地caffeine缓存工具类
 * @date 2023/08/01 22:13
 */
@Component
public class CaffeineUtil {

    @Resource
    private Cache<String, CacheDataWrapper> caffeineCache;

    // 添加缓存
    public <T> void put(final String key, final T value) {
        put(key, value, -1, null);
    }

    // 添加缓存并设置过期时间
    public <T> void put(final String key, final T value, final long delay, final TimeUnit unit) {
        final CacheDataWrapper cacheDataWrapper = new CacheDataWrapper(value, delay, unit);
        caffeineCache.put(key, cacheDataWrapper);
    }

    // 获取缓存
    public <T> Optional<T> get(final String key, final Class<T> clazz) {
        final CacheDataWrapper cacheDataWrapper = caffeineCache.getIfPresent(key);
        if (cacheDataWrapper != null) {
            return Optional.ofNullable(clazz.cast(cacheDataWrapper.getData()));
        }
        return Optional.empty();
    }

    // 移除缓存
    public void remove(final String key) {
        caffeineCache.invalidate(key);
    }

    // 清空缓存
    public void removeAll() {
        caffeineCache.invalidateAll();
    }

    /**
     * 为缓存项提供一个加载器。当缓存中不存在某个键时，加载器可以自动加载数据并将其存储到缓存中
     *
     * @param key    缓存key
     * @param clazz  缓存value类型
     * @param loader 加载器
     * @param <T>    缓存value类型
     * @return 缓存value
     */
    public <T> Optional<T> getOrLoad(final String key, final Class<T> clazz, final Function<String, T> loader) {
        final CacheDataWrapper cacheDataWrapper = caffeineCache.get(key, k -> new CacheDataWrapper(loader.apply(k), -1, null));
        return Optional.ofNullable(clazz.cast(cacheDataWrapper.getData()));
    }

    //带过期时间的加载器
    public <T> Optional<T> getOrLoad(final String key, final Class<T> clazz, final Function<String, T> loader, final long delay, final TimeUnit unit) {
        final CacheDataWrapper cacheDataWrapper = caffeineCache.get(key, k -> new CacheDataWrapper(loader.apply(k), delay, unit));
        return Optional.ofNullable(clazz.cast(cacheDataWrapper.getData()));
    }
}
