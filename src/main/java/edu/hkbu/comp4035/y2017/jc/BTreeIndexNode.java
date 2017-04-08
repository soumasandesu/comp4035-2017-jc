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
