package com.playground.ratelimiter;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowRateLimiter {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final Map<String, Deque<Long>> userRequestTimestamps = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    public boolean isAllowed(String userId) {
        long now = Instant.now().toEpochMilli();
        userRequestTimestamps.putIfAbsent(userId, new ArrayDeque<>());

        Deque<Long> timestamps = userRequestTimestamps.get(userId);

        synchronized (timestamps) {
            // Remove timestamps outside the window
            while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowSizeMillis) {
                timestamps.pollFirst();
            }

            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);
                return true;
            } else {
                return false;
            }
        }
    }

    // Optional cleanup method (if you want to prune stale users)
    public void cleanupStaleUsers(long ttlMillis) {
        long now = Instant.now().toEpochMilli();
        userRequestTimestamps.entrySet().removeIf(entry -> {
            Deque<Long> timestamps = entry.getValue();
            return timestamps.isEmpty() || now - timestamps.peekLast() > ttlMillis;
        });
    }
}

