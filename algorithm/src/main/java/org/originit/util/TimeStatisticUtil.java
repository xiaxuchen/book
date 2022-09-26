package org.originit.util;

import java.util.function.Supplier;


public class TimeStatisticUtil {

    public static void timing(Runnable runnable,String name) {
        final long start = System.currentTimeMillis();
        runnable.run();
        final long end = System.currentTimeMillis();
        final long span = end - start;
        System.out.printf("【%s】运行%d秒%d毫秒%n",name,span/1000,span%1000);
    }
}
