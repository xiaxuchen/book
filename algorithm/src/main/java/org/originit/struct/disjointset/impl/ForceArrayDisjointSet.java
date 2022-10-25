package org.originit.struct.disjointset.impl;

import org.originit.struct.disjointset.AbstractArrayDisjointSet;

public class ForceArrayDisjointSet extends AbstractArrayDisjointSet {
    public ForceArrayDisjointSet(int max) {
        super(max);
    }

    @Override
    protected void union(int parent, int child, int parentP, int childP) {
        parents[childP] = parentP;
    }

}
