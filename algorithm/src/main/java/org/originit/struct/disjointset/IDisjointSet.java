package org.originit.struct.disjointset;

public interface IDisjointSet {

    /**
     * 查询item的父节点 O(log n)
     */
    int find(int item);

    /**
     * 合并两个节点 O(log n),如果是DefaultArrayDisjointSet的则是O(n)
     */
    void union(int parent,int child);

    /**
     * 判断两个节点联通性,O(log n)
     */
    default boolean isConnected(int itemX,int itemY) {
        return find(itemX) == find(itemY);
    }
}
