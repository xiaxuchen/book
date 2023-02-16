package org.originit.shopcar.demo;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Test extends ClassLoader{

    public static void main(String[] args) {
     // 下载文件总数
     List<Integer> resultList = new ArrayList<>(100);
     IntStream.range(0,100).forEach(resultList::add);
     // 下载文件分段
     List<List<Integer>> split =  CollUtil.split(resultList, 10);

     ExecutorService executorService = new ThreadPoolExecutor(
             2,
             2,
             0L,
             TimeUnit.SECONDS,
             new LinkedBlockingQueue<>(2),
             new ThreadPoolExecutor.CallerRunsPolicy());
     CountDownLatch countDownLatch = new CountDownLatch(100);
     int l = 0;
     for (List<Integer> list : split) {
         System.out.println(l++);
         executorService.submit(() -> {
         list.forEach(i ->{
           try {
             // 模拟业务操作
             Thread.sleep(100);
             System.out.println("任务进入");
           } catch (InterruptedException e) {
             e.printStackTrace();
             System.out.println(e.getMessage());
           } finally {
             System.out.println(countDownLatch.getCount());
             countDownLatch.countDown();
           }
         });
       });
     }
     try {
         System.out.println("countDownLatch.await() start");
         countDownLatch.await();
       System.out.println("countDownLatch.await() end");
     } catch (InterruptedException e) {
       e.printStackTrace();
     }
   }


}
