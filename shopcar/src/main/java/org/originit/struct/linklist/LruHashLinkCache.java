package org.originit.struct.linklist;

import java.io.Serializable;

public class LruHashLinkCache implements Cache{
    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public boolean hasCache(String key) {
        return false;
    }

    @Override
    public Serializable getCache(String key) {
        return null;
    }

    @Override
    public void setCache(String key, Serializable serializable) {

    }

    @Override
    public void removeCache(String key) {

    }
}
