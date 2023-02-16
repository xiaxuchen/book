package org.originit.struct.linklist;

import java.io.Serializable;

public interface Cache {

    int getCapacity();

    boolean hasCache(String key);

    Serializable getCache(String key);

    void setCache(String key, Serializable serializable);

    void removeCache(String key);
}
