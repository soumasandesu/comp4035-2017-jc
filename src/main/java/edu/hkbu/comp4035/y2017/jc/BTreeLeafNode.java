package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public final class BTreeLeafNode<V> extends BTreeNode<V> {
    private BTreeLeafNode<V> nextLeaf;

    BTreeLeafNode(BTree tree) {
        super(tree);
    }

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
        boolean ok = !isSubItemsHungry() && subItems.contains(v);
        if (ok) {
            subItems.remove(v);
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
        return this.subItems.size() >= 2 * t() - 1;
    }

    @Override
    final boolean isSubItemsHungry() {
        // Every node other than the root must have at least t - 1 keys.
        // ** Count of sub items of a leaf node == count of keys.
        return getTree().getRootNode() != this && this.subItems.size() < t() - 1;
    }

    public final BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    public final void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
