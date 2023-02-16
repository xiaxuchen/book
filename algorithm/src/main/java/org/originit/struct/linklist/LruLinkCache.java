package org.originit.struct.linklist;

import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LruLinkCache implements Cache{

    protected static class Node {
        String key;

        Serializable value;

        Node next;

        Node pre;

        public Node() {
        }

        public Node(String key, Serializable value) {
            this.key = key;
            this.value = value;
        }
    }

    protected Node head = new Node();

    protected ReadWriteLock lock = new ReentrantReadWriteLock();


    protected int size = 0;

    private final int capacity;

    public LruLinkCache(int capacity) {
        this.capacity = capacity;
    }

    int getSize() {
        return size;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public boolean hasCache(String key) {
        return getNode(key) != null;
    }

    @Override
    public Serializable getCache(String key) {
        final Node node = getNode(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public void setCache(String key, Serializable serializable) {
        lock.writeLock().lock();
        try {
            if (hasCache(key)) {
                removeCache(key);
            } else if (this.size == capacity) {
                removeNode(head.pre);
                this.size--;
            }
            //   head -> 1 -> 2 -> 3 -> head
            //   head <- 1 <- 2 <- 3 <- head
            // 新建节点
            final Node node = new Node(key, serializable);
            // 插入到第一个节点
            if (head.next == null) {
                // 要维护node和head
                head.next = node;
                node.next = head;
                node.pre = head;
                head.pre = node;
            } else {
                node.next = head.next;
                head.next = node;
                node.pre = head;
                node.next.pre = node;
            }
            this.size++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeCache(String key) {
        lock.writeLock().lock();
        try {
            final Node node = getNode(key);
            removeNode(node);
            this.size--;
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected Node getNode(String key) {
        lock.readLock().lock();
        try {
            Node tmp = head;
            while (tmp.next != null && tmp.next != head) {
                tmp = tmp.next;
                if (tmp.key.equals(key)) {
                    return tmp;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    protected void removeNode (Node node){
        lock.writeLock().lock();
        try {
            if (node != null && node != head) {
                Node last = node.pre;
                last.next = node.next;
                if (node.next != null) {
                    node.next.pre = last;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
