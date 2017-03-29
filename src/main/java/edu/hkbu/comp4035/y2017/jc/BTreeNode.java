package edu.hkbu.comp4035.y2017.jc;

import java.io.Serializable;
import java.util.Vector;

// TODO: javadocs for BTreeNode<T>
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

    public abstract boolean isLeaf();

    boolean addKey(int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            ok = this.keys.add(key);
        }
        return ok;
    }

    private boolean isKeysFull() {
        return this.keys.size() >= 2 * t() - 1;
    }

    public boolean addSubItem(int key, S value) {
        boolean ok = addKey(key);
        if (ok) {
            ok = addSubItemValue(value);
        }
        return ok;
    }

    private boolean addSubItemValue(S s) {
        boolean ok = !isSubItemFull();
        if (ok) {
            ok = subItems.add(s);
        }
        return ok;
    }

    abstract boolean isSubItemFull();

    /* tricky methods goes below */

    int t() {
        return this.tree.getProperties().getDegree();
    }
}
