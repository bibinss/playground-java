package com.playground.ratelimiter;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowRateLimiter {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final long ttlMillis;
    private final Map<String, Window> userWindows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.ttlMillis = windowSizeMillis * 2; // keep stale data for at most 2 windows
    }

    public boolean isAllowed(String userId) {
        long now = Instant.now().toEpochMilli();
        long currentWindow = now / windowSizeMillis;

        // Clean up old/stale keys
        cleanupStaleWindows(now);

        userWindows.compute(userId, (key, window) -> {
            if (window == null || window.windowStart != currentWindow) {
                return new Window(currentWindow, 1, now);
            } else {
                window.requestCount++;
                window.lastUpdated = now;
                return window;
            }
        });

        return userWindows.get(userId).requestCount <= maxRequests;
    }

    private void cleanupStaleWindows(long now) {
        Iterator<Map.Entry<String, Window>> it = userWindows.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Window> entry = it.next();
            if (now - entry.getValue().lastUpdated > ttlMillis) {
                it.remove();
            }
        }
    }

    private static class Window {
        long windowStart;
        int requestCount;
        long lastUpdated;

        Window(long windowStart, int requestCount, long lastUpdated) {
            this.windowStart = windowStart;
            this.requestCount = requestCount;
            this.lastUpdated = lastUpdated;
        }
    }
}
