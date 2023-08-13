package org.Simbot.config.threadpool;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ycvk
 * @description 自定义线程工厂
 * @date 2023/08/13 16:11
 */
public class CustomThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public CustomThreadFactory(final String name) {
        this.namePrefix = name + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(final @NotNull Runnable r) {
        return new Thread(r, namePrefix + threadNumber.getAndIncrement());
        // 可以设置其他线程属性，如优先级等
    }
}
