package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public final class BTreeLeafNode<V> extends BTreeNode<V> {
    private final Vector<V> leavesValues;
    private BTreeLeafNode<V> nextLeaf;

    BTreeLeafNode(BTree tree) {
        super(tree);
        this.leavesValues = new Vector<V>();
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
        return (List<V>) leavesValues.clone();
    }

    @Override
    final Collection<V> getSubItems(int start_inclusive, int end_exclusive) {
        return leavesValues.subList(start_inclusive, end_exclusive);
    }

    @Override
    final V getSubItemAt(int index) {
        return leavesValues.get(index);
    }

    @Override
    final boolean removeSubItemValue(V v) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            ok = leavesValues.removeElement(v);
        }
        return ok;
    }

    @Override
    final boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            leavesValues.removeElementAt(index);
        }
        return ok;
    }

    final boolean isSubItemsFull() {
        return this.leavesValues.size() >= 2 * t() - 1;
    }

    @Override
    final boolean isSubItemsHungry() {
        return this.leavesValues.size() < t() - 1;
    }

    final public BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    final public void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
