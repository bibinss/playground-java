package com.playground;

import java.util.stream.IntStream;

public class Queue {

    static class QueueImpl {
        private int[] queue;
        private int i = 0;
        private int j = 0;

        public QueueImpl(int size) {
            queue = new int[size];
        }

        public int enqueue(int item) {
            if (j>i && (j-i) == queue.length) {
                throw new RuntimeException("Queue is full, cant enqueue anymore");
            }
            if (i>j && (j+queue.length-i) == queue.length) {
                throw new RuntimeException("Queue is full, cant enqueue anymore");
            }
            if (j == queue.length) {
                j=0;
            }
            queue[j] = item;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
            return item;
        }

        public int dequeue() {
            if (i==j) {
                throw new RuntimeException("No element in Queue, cant dequeue anymore");
            }
            if (i == queue.length) {
                i=0;
            }
            int item = queue[i];
            i++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return item;
        }

        public int size() {
            return (j-i);
        }
    }

    public static void main(String[] args) {
//        System.out.println("Happy Path execution");
//        System.out.println("********************");
//        happyPath();
//
//        System.out.println("Bound check enqueue");
//        System.out.println("********************");
//        boundCheckEnque();
//
//        System.out.println("Bound check dequeue");
//        System.out.println("********************");
//        boundCheckDequeue();

        System.out.println("Threadsafety check");
        System.out.println("********************");
        threadSafetyCheque();
    }

    private static void threadSafetyCheque() {
        int size = 1000;
        QueueImpl queue = new QueueImpl(size);
        try {
            System.out.println("Enqueue");
            IntStream.range(0, size).parallel().forEach( i -> System.out.print(queue.enqueue(i) + ", "));
            System.out.println();
            System.out.println("Queue size: " + queue.size());
            System.out.println("Dequeue");
            IntStream.range(0, size)
                    .parallel().forEach(i ->
                            System.out.print(queue.dequeue() + ", "));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void boundCheckDequeue() {
        QueueImpl queue = new QueueImpl(5);
        try {
            System.out.println("Enqueue");
            IntStream.range(0, 5).forEach( i -> System.out.print(queue.enqueue(i) + ", "));
            System.out.println();
            System.out.println("Dequeue");
            IntStream.range(0,6)
                    .forEach(i ->
                            System.out.println(queue.dequeue()));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void boundCheckEnque() {
        QueueImpl queue = new QueueImpl(5);
        try {
            System.out.println("Enqueue");
            IntStream.range(0, 6).forEach( i -> System.out.print(queue.enqueue(i) + ", "));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void happyPath() {
        QueueImpl queue = new QueueImpl(5);
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Enqueue");
            IntStream.range(0,5).forEach( i -> System.out.print(queue.enqueue(i) + ", "));
            System.out.println();
            System.out.println("Dequeue");
            IntStream.range(0,5)
                    .forEach(i ->
                            System.out.println(queue.dequeue()));
            System.out.println();
            System.out.println("Execution time: "
                    + (System.currentTimeMillis() - startTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}