package org.Simbot.config.threadpool;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author ycvk
 * @description 自定义io密集型线程池
 * @date 2023/08/13 16:11
 */
@Component
public class IOThreadPool {

    private static final ThreadPoolExecutor executor;

    static {
        final int corePoolSize = Runtime.getRuntime().availableProcessors() * 2; // 使用CPU核心数作为默认值

        final ThreadFactory customThreadFactory = new CustomThreadFactory("ioIntensive");

        executor = new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                customThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // 添加JVM关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(IOThreadPool::shutdown));
    }

    // 提交任务并根据任务类型调整线程池大小
    public static <T> Future<T> submit(final Callable<T> task) {
        return executor.submit(task);
    }

    // 关闭线程池
    public static void shutdown() {
        if (executor.isShutdown()) {
            return;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (final InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
