package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public final class BTreeIndexNode extends BTreeNode<BTreeNode> {
    BTreeIndexNode(BTree tree) {
        super(tree);
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
        boolean ok = !isSubItemsHungry();
        if (ok) {
            ok = subItems.removeElement(bTreeIndexNode);
        }
        return ok;
    }

    @Override
    final boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            subItems.removeElementAt(index);
        }
        return ok;
    }

    final boolean isSubItemsFull() {
        return this.subItems.size() >= 2 * t();
    }

    @Override
    final boolean isSubItemsHungry() {
        return this.subItems.size() < t();
    }
}
