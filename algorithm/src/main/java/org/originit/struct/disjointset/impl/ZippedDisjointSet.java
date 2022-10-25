package org.originit.struct.disjointset.impl;

public class ZippedDisjointSet extends RankedArrayDisjointSet {
    public ZippedDisjointSet(int max) {
        super(max);
    }

    @Override
    public int find(int item) {
        int parent = item;
        while (parents[parent] != parent) {
            parents[parent] = parents[parents[parent]];
            parent = parents[parent];
        }
        return parent;
    }
}
