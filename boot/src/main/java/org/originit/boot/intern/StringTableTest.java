package org.originit.boot.intern;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Random;

public class StringTableTest {

    public static void main(String[] args) {
        ObjectIdGenerators.StringIdGenerator generator = new ObjectIdGenerators.StringIdGenerator();
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            final String intern = (i + "哈哈哈" + i).intern();
        }
        final long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
