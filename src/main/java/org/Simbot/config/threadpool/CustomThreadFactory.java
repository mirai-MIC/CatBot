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
    private final int threadPriority;

    public CustomThreadFactory(final String name, final int threadPriority) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.namePrefix = name + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
        this.threadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final @NotNull Runnable r) {
        final Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        thread.setPriority(threadPriority);
        return thread;
    }
}
