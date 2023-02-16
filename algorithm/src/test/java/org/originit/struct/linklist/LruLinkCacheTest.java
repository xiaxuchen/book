package org.originit.struct.linklist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LruLinkCacheTest {


    @Test
    void testSetCache() {
        LruLinkCache cache = new LruLinkCache(10);
        for (int i = 0; i < 12; i++) {
            cache.setCache(String.valueOf(i),i);
            if (i == 8) {
                cache.setCache("0", 0);
            }
        }
        assertEquals(cache.getCapacity(), 10);
        assertEquals(cache.getSize(), 10);
        assertTrue(cache.hasCache(String.valueOf(0)));
        assertFalse(cache.hasCache(String.valueOf(1)));
        assertTrue(cache.hasCache(String.valueOf(11)));
        assertFalse(cache.hasCache(String.valueOf(12)));
    }

    @Test
    void testRemove() {
        LruLinkCache cache = new LruLinkCache(1);
        cache.setCache("xxc","god");
        cache.setCache("zss","ai ni");
        assertFalse(cache.hasCache("xxc"));
        assertTrue(cache.getCache("zss").equals("ai ni"));
        cache.removeCache("zss");
        assertFalse(cache.hasCache("zss"));
    }


    @Test
    void testHashSetCache() {
        LruLinkCache cache = new LruHashLinkCache(10);
        for (int i = 0; i < 12; i++) {
            cache.setCache(String.valueOf(i),i);
            if (i == 8) {
                cache.setCache("0", 0);
            }
        }
        assertEquals(cache.getCapacity(), 10);
        assertEquals(cache.getSize(), 10);
        assertTrue(cache.hasCache(String.valueOf(0)));
        assertFalse(cache.hasCache(String.valueOf(1)));
        assertTrue(cache.hasCache(String.valueOf(11)));
        assertFalse(cache.hasCache(String.valueOf(12)));
    }

    @Test
    void testHashRemove() {
        LruLinkCache cache = new LruHashLinkCache(1);
        cache.setCache("xxc","god");
        cache.setCache("zss","ai ni");
        assertFalse(cache.hasCache("xxc"));
        assertTrue(cache.getCache("zss").equals("ai ni"));
        cache.removeCache("zss");
        assertFalse(cache.hasCache("zss"));
    }
}