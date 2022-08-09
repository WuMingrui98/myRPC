package xyz.raygetoffer.utils.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 线程池(ThreadPool)工具类
 *
 * @author mingruiwu
 * @create 2022/6/29 15:20
 * @description
 */
@Slf4j
public final class ThreadPoolFactoryUtil {
    /**
     * 通过 threadNamePrefix 来区分不同线程池（我们可以把相同 threadNamePrefix 的线程池看作是为同一业务场景服务）。
     */
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();


    /**
     * 关闭所有线程池
     */
    public static void shutdownAllThreadPool() {
        log.info("call shutDownAllThreadPool method");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            log.info("shut down thread pool [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("Thread pool never terminated, shutdown thread pool now");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                executorService.shutdownNow();
            }
        });
    }

    /**
     * 如果ConcurrentHashMap中不存在则创建自定义线程池（最完整的方法）
     * @param customThreadPoolConfig 线程池配置
     * @param threadNamePrefix       线程名前缀
     * @param daemon                 指定是否为 Daemon Thread(守护线程)
     * @return
     */
    public static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, key ->
            creatThreadPool(customThreadPoolConfig, threadNamePrefix, daemon)
        );
        // 如果线程池shutdown或terminate的话，重新创建一个
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = creatThreadPool(customThreadPoolConfig, threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }


    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix) {
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    /**
     * 创建线程池（最完整的方法）
     * @param customThreadPoolConfig 线程池配置
     * @param threadNamePrefix 线程前缀
     * @param daemon 是否为守护线程
     * @return 创建的线程池
     */
     static ExecutorService creatThreadPool(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(customThreadPoolConfig.getCorePoolSize(), customThreadPoolConfig.getMaximumPoolSize(),
                customThreadPoolConfig.getKeepAliveTime(), customThreadPoolConfig.getUnit(), customThreadPoolConfig.getBlockingQueue(),
                threadFactory);
    }


    /**
     * 创建ThreadFactory。如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     *
     * @param threadNamePrefix  作为创建的线程名字的前缀
     * @param daemon            指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (!StringUtils.isEmpty(threadNamePrefix)) {
            ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
            if (daemon != null) {
                threadFactoryBuilder.setDaemon(daemon);
            }
            return threadFactoryBuilder.setNameFormat(threadNamePrefix + "-%d").build();
        }
        return Executors.defaultThreadFactory();
    }


    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池实例
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.schedule(() -> {
            log.info("============ThreadPool Status=============");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            log.info("Number of Tasks : [{}]", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("===========================================");
        }, 0, TimeUnit.SECONDS);
    }
}
