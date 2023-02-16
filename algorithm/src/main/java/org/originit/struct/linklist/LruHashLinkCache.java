package org.originit.struct.linklist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LruHashLinkCache extends LruLinkCache{

    public LruHashLinkCache(int capacity) {
        super(capacity);
    }

    private Map<String,Node> nodeMap = new HashMap<>();

    @Override
    protected Node getNode(String key) {
        lock.readLock().lock();
        try {
            return nodeMap.get(key);
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected void removeNode(Node node) {
        lock.writeLock().lock();
        try {
            super.removeNode(node);
            nodeMap.remove(node.key);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setCache(String key, Serializable serializable) {
        lock.writeLock().lock();
        try {
            super.setCache(key, serializable);
            nodeMap.put(key,head.next);
        }finally {
            lock.writeLock().unlock();
        }
    }


}
