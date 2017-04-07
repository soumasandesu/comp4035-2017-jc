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

    @Override
    final BTreeNode getSubItemAt(int index) {
        return subItems.get(index);
    }

    @Override
    final Collection<BTreeNode> getSubItems() {
        //noinspection unchecked
        return (List<BTreeNode>) subItems.clone();
    }

    @Override
    final Collection<BTreeNode> getSubItems(int start_inclusive, int end_exclusive) {
        return subItems.subList(start_inclusive, end_exclusive);
    }

    @Override
    final boolean removeSubItemValue(BTreeNode bTreeIndexNode) {
        boolean ok = !isSubItemsHungry() && subItems.contains(bTreeIndexNode);
        if (ok) {
            subItems.remove(bTreeIndexNode);
        }
        return ok;
    }

    @Override
    final boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry() && subItems.size() > index;
        if (ok) {
            subItems.remove(index);
        }
        return ok;
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
