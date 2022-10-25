package org.originit.struct.disjointset;

public abstract class AbstractArrayDisjointSet implements IDisjointSet {

    protected final int[] parents;

    public AbstractArrayDisjointSet(int max) {
        parents = new int[max];
        // 父节点是自己
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }
    }

    @Override
    public int find(int item) {
        int parent = item;
        while (parents[parent] != parent) {
            parent = parents[parent];
        }
        return parent;
    }

    @Override
    public void union(int parent, int child) {
        final int parentP = find(parent);
        final int childP = find(child);
        if (parentP == childP) {
            return;
        }
        union(parent,child,parentP,childP);
    }

    protected abstract void union(int parent, int child, int parentP, int childP);

}
