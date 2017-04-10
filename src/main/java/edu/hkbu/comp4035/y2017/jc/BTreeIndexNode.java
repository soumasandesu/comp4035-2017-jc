package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
/**
 * Represents an index node in the B+ Tree.
 */
public final class BTreeIndexNode extends BTreeNode<BTreeNode> {
    /**
     * Creates an index node.
     * @param tree The respective B+ tree.
     */
    BTreeIndexNode(BTree tree) {
        super(tree);
    }

    /**
     * Creates an index node with pre-stored data.
     * @param tree The respective B+ tree.
     * @param keys The pre-stored keys.
     * @param subItems The pre-stored sub-nodes.
     */
    public BTreeIndexNode(BTree tree, LinkedList<Integer> keys, LinkedList<BTreeNode> subItems) {
        super(tree, keys, subItems);
    }

    @Override
    public final boolean isLeaf() {
        return false;
    }

    @Override
    final BTreeIndexNode getEmptyClone() {
        return new BTreeIndexNode(this.getTree());
    }

    final boolean isSubItemsFull() {
        return this.subItems.size() >= 2 * t();
    }

    @Override
    final boolean isSubItemsHungry() {
        return isSubItemsHungry(0);
    }
    
    @Override
    final boolean isSubItemsHungry(int sizeAdd) {
        // Every internal node other than the root thus has at least t children.
        return getTree().getRootNode() != this && this.subItems.size()+sizeAdd < t();
    }
}
