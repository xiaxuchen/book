package org.originit.struct.disjointset.impl;

import org.originit.struct.disjointset.AbstractArrayDisjointSet;

import java.util.Arrays;

public class RankedArrayDisjointSet extends AbstractArrayDisjointSet {

    protected final int[] rank;

    public RankedArrayDisjointSet(int max) {
        super(max);
        rank = new int[max];
        Arrays.fill(rank, 1);
    }

    @Override
    protected void union(int parent, int child, int parentP, int childP) {
        if (rank[parentP] == rank[childP]) {
            parents[childP] = parentP;
            rank[parentP]++;
            return;
        }
        if (rank[parentP] > rank[childP]) {
            parents[childP] = parentP;
        } else {
            parents[parentP] = childP;
        }
    }
}
