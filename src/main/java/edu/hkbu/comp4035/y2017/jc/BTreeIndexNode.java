package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public final class BTreeIndexNode extends BTreeNode<BTreeNode> {
    BTreeIndexNode(BTree tree) {
        super(tree);
    }

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

    final boolean isKeysFull() {
        // max deg of keys = 2t - 1
        return n() >= 2 * t() - 1;
    }

    final boolean isKeysHungry() {
        // Every node other than the root must have at least t - 1 keys.
        // min deg of keys = t - 1
        return getTree().getRootNode() != this && n() < t() - 1;
    }

    final boolean isSubItemsFull() {
        return this.subItems.size() >= 2 * t();
    }

    @Override
    final boolean isSubItemsHungry() {
        // Every internal node other than the root thus has at least t children.
        return getTree().getRootNode() != this && this.subItems.size() < t();
    }
}
