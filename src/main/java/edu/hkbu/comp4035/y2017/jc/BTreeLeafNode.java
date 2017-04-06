package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public final class BTreeLeafNode<V> extends BTreeNode<V> {
    private BTreeLeafNode<V> nextLeaf;

    BTreeLeafNode(BTree tree) {
        super(tree);
    }

    @Override
    public final boolean isLeaf() {
        return true;
    }

    @Override
    final BTreeLeafNode<V> getEmptyClone() {
        return new BTreeLeafNode<V>(this.getTree());
    }

    @Override
    final Collection<V> getSubItems() {
        //noinspection unchecked
        return (List<V>) subItems.clone();
    }

    @Override
    final Collection<V> getSubItems(int start_inclusive, int end_exclusive) {
        return subItems.subList(start_inclusive, end_exclusive);
    }

    @Override
    final V getSubItemAt(int index) {
        return subItems.get(index);
    }

    @Override
    final boolean removeSubItemValue(V v) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            ok = subItems.removeElement(v);
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
        return this.subItems.size() >= 2 * t() - 1;
    }

    @Override
    final boolean isSubItemsHungry() {
        return this.subItems.size() < t() - 1;
    }

    final public BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    final public void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
