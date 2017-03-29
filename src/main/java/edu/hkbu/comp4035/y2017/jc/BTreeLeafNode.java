package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class BTreeLeafNode<V> extends BTreeNode<V> {
    private final Vector<V> leavesValues;
    private BTreeLeafNode<V> nextLeaf;

    BTreeLeafNode(BTree tree) {
        super(tree);
        this.leavesValues = new Vector<V>();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    BTreeLeafNode<V> getEmptyClone() {
        return new BTreeLeafNode<V>(this.getTree());
    }

    @Override
    Collection<V> getSubItems() {
        //noinspection unchecked
        return (List<V>) leavesValues.clone();
    }

    @Override
    Collection<V> getSubItems(int start_inclusive, int end_exclusive) {
        return leavesValues.subList(start_inclusive, end_exclusive);
    }

    @Override
    V getSubItemAt(int index) {
        return leavesValues.get(index);
    }

    @Override
    boolean removeSubItemValue(V v) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            ok = leavesValues.removeElement(v);
        }
        return ok;
    }

    @Override
    boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            leavesValues.removeElementAt(index);
        }
        return ok;
    }

    boolean isSubItemsFull() {
        return this.leavesValues.size() >= 2 * t() - 1;
    }

    @Override
    boolean isSubItemsHungry() {
        return this.leavesValues.size() < t() - 1;
    }

    public BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    public void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
