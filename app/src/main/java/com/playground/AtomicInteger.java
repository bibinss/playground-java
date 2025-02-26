package com.playground;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class AtomicInteger {

    private volatile int i;
    private static final VarHandle VALUE;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE = l.findVarHandle(AtomicInteger.class, "i", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public int addAndGet()
    {
        for(;;) {
            int current = i;
            int next = current + 1;
            if (VALUE.compareAndSet(this, current, next)) {
                return next;
            }
        }
    }

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 100).forEach(
                i -> executorService.submit(() -> {
                    System.out.println(atomicInteger.addAndGet());
                })
        );
    }
}
