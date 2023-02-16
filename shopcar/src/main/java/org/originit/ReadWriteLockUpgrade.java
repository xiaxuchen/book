package org.originit;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockUpgrade {


    static ReadWriteLock rwl = new ReentrantReadWriteLock();

    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();

    static void upgrade () {
        w.lock();
        try {
            r.lock();
            try {
                System.out.println("hayha");
            } finally {
                r.unlock();
            }
        } finally {
            w.unlock();
        }

    }


    public static void main(String[] args) {
        upgrade();
    }
}
