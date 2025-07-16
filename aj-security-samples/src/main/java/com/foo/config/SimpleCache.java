package com.foo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleCache extends ConcurrentHashMap<String, SimpleCache.Item> {
    private final ScheduledExecutorService scheduler;

    private volatile boolean running = true;

    /**
     * 缓存项
     */
    @Data
    public static class Item {
        String value;
        int expireSeconds;
        long addTime;

        public Item(String value, int expireSeconds) {
            this.value = value;
            this.expireSeconds = expireSeconds;
            addTime = System.currentTimeMillis();
        }
    }

    /**
     * 构造函数，初始化定时器
     *
     * @param scanIntervalSeconds 扫描间隔（秒）
     */
    public SimpleCache(int scanIntervalSeconds) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        startExpirationScanner(scanIntervalSeconds);
    }

    /**
     * 默认构造函数，扫描间隔为 5 秒
     */
    public SimpleCache() {
        this(5);
    }

    /**
     * 添加缓存项
     *
     * @param key           键
     * @param value         值
     * @param expireSeconds 过期时间（秒）
     */
    public void add(String key, String value, int expireSeconds) {
        put(key, new Item(value, expireSeconds));
        log.debug("Added item with key: {}, value: {}, expireSeconds: {}", key, value, expireSeconds);
    }

    /**
     * 启动定时扫描任务，删除过期项
     *
     * @param scanIntervalSeconds 扫描间隔（秒）
     */
    private void startExpirationScanner(int scanIntervalSeconds) {
        scheduler.scheduleAtFixedRate(() -> {
            if (running) {
                try {
                    scanAndRemoveExpiredItems();
                } catch (Exception e) {
                    log.error("Error during cache expiration scan", e);
                }
            }
        }, scanIntervalSeconds, scanIntervalSeconds, TimeUnit.SECONDS);

        log.info("Started cache expiration scanner with interval: {} seconds", scanIntervalSeconds);
    }

    /**
     * 扫描并删除过期项
     */
    private void scanAndRemoveExpiredItems() {
        long currentTime = System.currentTimeMillis();

        forEach((key, item) -> {
            long elapsedSeconds = (currentTime - item.addTime) / 1000;

            if (elapsedSeconds > item.expireSeconds) {
                remove(key);
                log.debug("Removed expired item with key: {}", key);
            }
        });
    }

    /**
     * 优雅关闭定时器
     */
    public void shutdown() {
        running = false;
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                log.warn("Scheduler did not terminate gracefully");
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
            log.warn("Interrupted during scheduler shutdown", e);
        }

        log.info("SimpleCache scheduler shut down");
    }
}
