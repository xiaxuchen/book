package org.originit;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.*;

public class CASTest {

    private volatile String name;

    static LongAdder accumulator = new LongAdder();

    public static void main(String[] args) {
    }
}
