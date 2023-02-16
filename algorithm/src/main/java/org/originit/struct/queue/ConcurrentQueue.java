package org.originit.struct.queue;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentQueue implements Queue{

    protected static class Node {
        Serializable value;
        Node next;
    }

    AtomicReference<Node> head;
    AtomicReference<Node> tail;


    @Override
    public void enqueue(Serializable serializable) {
        Node pre;
        Node newNode = new Node();
        newNode.value = serializable;
        do {
            // 一开始get到的是node
            pre = tail.get();
        } while (!head.compareAndSet(pre,newNode));
        // 在这里执行出队
        pre.next = newNode;
        size.incrementAndGet();
    }

    @Override
    public Serializable poll() {
        Node node;
        Node node1;
        do {
            node = head.get();
            node1 = node.next;
        }while (head.compareAndSet(node, node1));
        if (node1 != null) {
            size.decrementAndGet();
            return node1.value;
        }
        return null;
    }

    protected AtomicInteger size = new AtomicInteger(0);

    public ConcurrentQueue() {
        final Node node = new Node();
        this.head = new AtomicReference<>(node);
        this.tail = new AtomicReference<>(node);
    }

    @Override
    public int getSize() {
        return size.get();
    }
}
