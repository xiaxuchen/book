package org.originit.shopcar.demo;

import org.originit.struct.linklist.Cache;
import org.originit.struct.linklist.LruHashLinkCache;

public class CacheTest {

    public static int age = 11;

    public static void main(String[] args) {
        Cache cache = new LruHashLinkCache();
        cache.setCache("测试","???");
        System.out.println(cache.hasCache("测试"));
    }
}
