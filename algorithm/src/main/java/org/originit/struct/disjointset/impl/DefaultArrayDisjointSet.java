package org.originit.struct.disjointset.impl;

import org.originit.struct.disjointset.AbstractArrayDisjointSet;

public class DefaultArrayDisjointSet extends AbstractArrayDisjointSet {
    public DefaultArrayDisjointSet(int max) {
        super(max);
    }

    @Override
    protected void union(int parent, int child, int parentP, int childP) {
        for (int i = 0; i < parents.length; i++) {
            if (parents[i] == childP) {
                parents[i] = parentP;
            }
        }
    }

}
