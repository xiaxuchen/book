package org.originit.shopcar;

import org.originit.struct.linklist.Cache;
import org.originit.struct.linklist.LruHashLinkCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class ShopcarApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShopcarApplication.class)
                .listeners((ApplicationListener<ApplicationStartingEvent>) event -> {
                    System.out.println("starting");
                },(ApplicationListener<ApplicationStartedEvent>) event -> {
                    System.out.println("started");
                })
                .run( args);
    }

    static class Test {

        static volatile  int a;
        void test() {
            while (a > 0) {
                System.out.println(a);
            }
            int i = 0;
            i++;
            int a = ++i;
            int b = i++;
            Cache cache = new LruHashLinkCache();
            cache.setCache("测试","???");
            System.out.println(cache.hasCache("测试"));
        }
    }
}
