package com.playground.ratelimiter;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter {

    private final int capacity;
    private final double refillRatePerMillis;
    private final Map<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(int capacity, double refillTokensPerSecond) {
        this.capacity = capacity;
        this.refillRatePerMillis = refillTokensPerSecond / 1000.0;
    }

    public boolean isAllowed(String userId) {
        long now = Instant.now().toEpochMilli();
        userBuckets.putIfAbsent(userId, new TokenBucket(capacity, now));

        TokenBucket bucket = userBuckets.get(userId);

        synchronized (bucket) {
            bucket.refill(now, refillRatePerMillis, capacity);

            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                return true;
            } else {
                return false;
            }
        }
    }

    private static class TokenBucket {
        double tokens;
        long lastRefillTimestamp;

        TokenBucket(int capacity, long now) {
            this.tokens = capacity;
            this.lastRefillTimestamp = now;
        }

        void refill(long now, double ratePerMillis, int capacity) {
            long elapsed = now - lastRefillTimestamp;
            double refill = elapsed * ratePerMillis;
            this.tokens = Math.min(capacity, this.tokens + refill);
            this.lastRefillTimestamp = now;
        }
    }
}

