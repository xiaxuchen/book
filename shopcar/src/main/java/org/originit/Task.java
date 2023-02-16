package org.originit;// 创建任务T2的FutureTask

import java.util.concurrent.*;

class Task {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final long start = System.currentTimeMillis();
        CompletableFuture<String> tea = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "tea";
        });
        CompletableFuture<String> water = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "烧水";
        });
        water.applyToEitherAsync(tea, s -> {
            return s;
        }).thenAccept(s -> {
            System.out.println(s);
        }).join();
        final long end = System.currentTimeMillis();
        System.out.println(end - start);

    }
    static class T1Task implements Callable<String> {
        FutureTask<String> ft2;
        // T1任务需要T2任务的FutureTask
        T1Task(FutureTask<String> ft2){
            this.ft2 = ft2;
        }
        @Override
        public String call() throws Exception {
            System.out.println("T1:洗水壶...");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("T1:烧开水...");
            TimeUnit.SECONDS.sleep(15);
// 获取T2线程的茶叶
            String tf = ft2.get();
            System.out.println("T1:拿到茶叶:"+tf);
            System.out.println("T1:泡茶...");
            return "上茶:" + tf;
        }
    }
    // T2Task需要执行的任务:
// 洗茶壶、洗茶杯、拿茶叶
    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T2:洗茶壶...");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("T2:洗茶杯...");
            TimeUnit.SECONDS.sleep(2);
            System.out.println("T2:拿茶叶...");
            TimeUnit.SECONDS.sleep(1);
            return "⻰井";
        }
    }
// 一次
}