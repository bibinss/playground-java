package com.playground;

import java.util.stream.IntStream;

public class Queue {

    static class QueueImpl {
        private int[] queue;
        private int i = 0;
        private int j = 0;

        private int buffer = 0;

        public QueueImpl(int size) {
            queue = new int[size];
        }

        public void enqueue(int item) {
            if (j>i && (j-i) == queue.length) {
                throw new RuntimeException("Queue is full");
            }
            if (i>j && (j+queue.length-i) == queue.length) {
                throw new RuntimeException("Queue is full");
            }
            if (j == queue.length) {
                j=0;
            }
            queue[j] = item;
            j++;
        }

        public int dequeue() {
            if (i==j) {
                throw new RuntimeException("No element in Queue");
            }
            if (i == queue.length) {
                i=0;
            }
            int item = queue[i];
            i++;
            return item;
        }
    }

    public static void main(String[] args) {
        QueueImpl queue = new QueueImpl(5);
        try {
            IntStream.range(0,5).forEach( i -> queue.enqueue(i));

            IntStream.range(0,5)
                    .forEach(i ->
                            System.out.println(queue.dequeue()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}