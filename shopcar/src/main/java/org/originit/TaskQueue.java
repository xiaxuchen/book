package org.originit;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

    private volatile Integer[] data;

    private int start = 0;

    private int end = 0;

    ReentrantLock lock = new ReentrantLock(true);

    Condition isNotEmpty = lock.newCondition();

    Condition isNotFull = lock.newCondition();

    public TaskQueue(int size) {
        this.data = new Integer[size];
    }

    public void produce(Integer product) {
        lock.lock();
        try {
            while (this.isFull()) {
                try {
                    isNotFull.await();
                } catch (InterruptedException e) {
                }
            }
            data[end] = product;
            end = (end + 1) % data.length;
            isNotEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Integer consume() {
        lock.lock();
        try {
            while (isEmpty()) {
                try {
                    isNotEmpty.await();
                } catch (InterruptedException e) {
                }
            }
            Integer res = data[start];
            // 消费队头
            start = (start + 1) % data.length;
            isNotFull.signal();
            return res;
        }finally {
            lock.unlock();
        }
    }

    private boolean isFull() {
        return (end + 1) % data.length == start;
    }

    private boolean isEmpty() {
        return start == end;
    }

    public static void main(String[] args) {
        final TaskQueue taskQueue = new TaskQueue(10);
        final Thread producer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                taskQueue.produce(i);
            }
        });
        producer.start();
        for (int i = 0; i < 4; i++) {
            Thread consumer = new Thread(() -> {
                while(true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 消费者
                    System.out.println(Thread.currentThread().getName() + ":" + taskQueue.consume());
                }
            });
            // 守护线程
            consumer.setDaemon(true);
            consumer.setName("consumer" + i);
            consumer.start();
        }


    }
}
