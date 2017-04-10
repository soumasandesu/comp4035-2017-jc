package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Represents a leaf node in the B+ Tree.
 * @param <V> The value type of B+ tree.
 */
public final class BTreeLeafNode<V> extends BTreeNode<V> {
    private BTreeLeafNode<V> nextLeaf;

    /**
     * Creates a leaf node.
     * @param tree The respective B+ tree.
     */
    BTreeLeafNode(BTree tree) {
        super(tree);
    }

    /**
     * Creates a leaf node with pre-stored data.
     * @param tree The respective B+ tree.
     * @param keys The pre-stored keys.
     * @param subItems The pre-stored values.
     * @param nextLeaf The object reference to next node.
     */
    public BTreeLeafNode(BTree tree, LinkedList<Integer> keys, LinkedList<V> subItems, BTreeLeafNode<V> nextLeaf) {
        super(tree, keys, subItems);
        this.nextLeaf = nextLeaf;
    }

    @Override
    public final boolean isLeaf() {
        return true;
    }

    @Override
    final BTreeLeafNode<V> getEmptyClone() {
        return new BTreeLeafNode<>(this.getTree());
    }

    final boolean isSubItemsFull() {
        return this.subItems.size() >= 2 * t() - 1;
    }

    @Override
    final boolean isSubItemsHungry() {
        return isSubItemsHungry(0);
    }
    
    @Override
    final boolean isSubItemsHungry(int sizeAdd) {
        // Every node other than the root must have at least t - 1 keys.
        // ** Count of sub items of a leaf node == count of keys.
        return getTree().getRootNode() != this && this.subItems.size()+sizeAdd < t() - 1;
    }

    /**
     * Gets the next leaf node after this node.
     * @return The next leaf.
     */
    public final BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    /**
     * Sets the next leaf node after this node.
     * @param nextLeaf The next leaf.
     */
    public final void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
