package org.originit.struct.queue;

import java.io.Serializable;

public interface Queue {

    void enqueue(Serializable serializable);

    Serializable poll();

    int getSize();

}
