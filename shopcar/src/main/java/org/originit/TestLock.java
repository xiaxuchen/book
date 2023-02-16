package org.originit;

import java.util.Deque;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

public class TestLock {

    ExecutorService executorService = new ThreadPoolExecutor(10,10,
            3, TimeUnit.MINUTES ,new LinkedBlockingDeque<>(20), new ThreadFactory() {
        AtomicInteger threadId = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setName("Fixed" + threadId.getAndIncrement());
            return thread;
        }
    },new ThreadPoolExecutor.CallerRunsPolicy());

    Semaphore semaphore = new Semaphore(10);

    static CountDownLatch countDownLatch = new CountDownLatch(60);

    public String asyncTask() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
            countDownLatch.countDown();
        }
        return "xxc";
    }

    public void syncTask() {
        for (int i = 0; i < 60; i++) {
            executorService.submit(this::asyncTask);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final long l = System.currentTimeMillis();
        final TestLock testLock = new TestLock();
        testLock.usingCountDownLatch();
    }

    Deque<String> orders = new LinkedBlockingDeque<>();

    Deque<String> products = new LinkedBlockingDeque<>();

    CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
        executorService.submit(() -> {
            System.out.println(check(orders.pop(), products.pop()));
        });
    });

    AtomicInteger orderId = new AtomicInteger();

    AtomicInteger productId = new AtomicInteger();

    String getOrders() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            orders.add("orders" + orderId.getAndIncrement());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void getProduct() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            products.add("product" + productId.getAndIncrement());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    String check(String order, String product) {
        return order + product;
    }

    void usingCountDownLatch() {
        executorService.submit(this::getOrders);
        executorService.submit(this::getProduct);
    }

}
