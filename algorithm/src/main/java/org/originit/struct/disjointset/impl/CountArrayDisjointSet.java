package org.originit.struct.disjointset.impl;

import org.originit.struct.disjointset.AbstractArrayDisjointSet;

public class CountArrayDisjointSet extends AbstractArrayDisjointSet {

    protected final int[] count;

    public CountArrayDisjointSet(int max) {
        super(max);
        count = new int[max];
        for (int i = 0; i < count.length; i++) {
            count[i] = 1;
        }
    }

    @Override
    protected void union(int parent, int child, int parentP, int childP) {
        if (count[parentP] < count[childP]) {
            parents[parentP] = childP;
            count[childP] += count[childP];
        } else {
            parents[childP] = parentP;
            count[parentP] += count[parentP];
        }
    }
}
