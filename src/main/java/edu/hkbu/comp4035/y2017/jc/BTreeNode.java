package edu.hkbu.comp4035.y2017.jc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/*
    All of the `BTreeNodes` should only have the pointer of its child. The operation to get parent should ... not get,
    but should have already been recorded by `BTree*Operations` whilst traversing through different levels.
 */
// TODO: javadocs for BTreeNode<S>
public abstract class BTreeNode<S> implements Serializable {
    private final BTree tree;
    private final Vector<Integer> keys;
    private final Vector<S> subItems;

    // TODO: more ctors
    BTreeNode(BTree tree) {
        this.tree = tree;
        keys = new Vector<>();
        subItems = new Vector<>();
    }

    final BTreeNode<S> deepClone() {
        BTreeNode<S> clone = getEmptyClone();
        //noinspection unchecked
        clone.keys.addAll((Vector<Integer>) keys.clone());
        //noinspection unchecked
        clone.subItems.addAll((Collection<? extends S>) subItems.clone());
        return clone;
    }

    public final BTree getTree() {
        return tree;
    }

    public abstract boolean isLeaf();

    abstract BTreeNode<S> getEmptyClone();

    abstract Collection<S> getSubItems();

    abstract Collection<S> getSubItems(int start_inclusive, int end_exclusive);

    final boolean addKey(int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            ok = this.keys.add(key);
        }
        return ok;
    }

    final boolean addKeyAt(int index, int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            this.keys.add(index, key);
        }
        return ok;
    }

    final boolean addKeys(Collection<Integer> keys) {
        boolean ok;
        for (Integer key : keys) {
            ok = addKey(key);
            if (!ok) return false;
        }
        return true;
    }

    final boolean removeKey(int key) {
        boolean ok = !isKeysHungry();
        if (ok) {
            ok = keys.removeElement(key);
        }
        return ok;
    }

    final boolean removeKeyAt(int index) {
        boolean ok = !isKeysHungry();
        if (ok) {
            keys.removeElementAt(index);
        }
        return ok;
    }

    final boolean removeKeys(Collection<Integer> keys) {
        boolean ok;
        for (Integer key : keys) {
            ok = removeKey(key);
            if (!ok) return false;
        }
        return true;
    }

    final int getKeyAt(int index) {
        return keys.get(index);
    }

    final Collection<Integer> getKeys() {
        //noinspection unchecked
        return (Collection<Integer>) keys.clone();
    }

    final Collection<Integer> getKeys(int start_inclusive, int end_exclusive) {
        return keys.subList(start_inclusive, end_exclusive);
    }

    final boolean isKeysFull() {
        // max deg of keys = 2t - 1
        return n() >= 2 * t() - 1;
    }

    final boolean isKeysHungry() {
        // min deg of keys = t
        return n() < t();
    }

    final boolean addSubItemValue(S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            ok = subItems.add(s);
        }
        return ok;
    }

    final boolean addSubItemValueAt(int index, S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            subItems.add(index, s);
        }
        return ok;
    }

    final boolean addSubItemValues(Collection<S> ss) {
        boolean ok = true;
        for (S s : ss) {
            ok = this.addSubItemValue(s);
        }
        return ok;
    }

    abstract S getSubItemAt(int index);

    abstract boolean removeSubItemValue(S s);

    abstract boolean removeSubItemValueAt(int index);

    final boolean removeSubItemsValues(Collection<S> ss) {
        boolean ok;
        for (S s : ss) {
            ok = removeSubItemValue(s);
            if (!ok) return false;
        }
        return true;
    }

    abstract boolean isSubItemsFull();

    abstract boolean isSubItemsHungry();

    /* tricky methods goes below */

    final int t() {
        return this.tree.getProperties().getDegree();
    }

    final int n() {
        return this.keys.size();
    }
}
