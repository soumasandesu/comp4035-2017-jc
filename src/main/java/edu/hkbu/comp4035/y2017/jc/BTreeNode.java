package edu.hkbu.comp4035.y2017.jc;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: javadocs for BTreeNode<T>
public class BTreeNode<V> implements Serializable {
    private final BTree tree;
    private final ArrayList<BTreeNode<V>> leaves;
    private int key;
    private V value;

    // TODO: ctors


    public BTreeNode(BTree tree) {
        this.tree = tree;
        this.key = 0;
        this.value = null;
        this.leaves = new ArrayList<>(2 * t());
    }

    public BTreeNode<V> getLeaveAt(int index) {
        return this.leaves.get(index);
    }

    public boolean isLeaf() {
        return leaves.size() > 0;
    }

    public int getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public BTreeNode<V> getSubNode(int index) {
        BTreeNode<V> ret = null;

        for (BTreeNode<V> l : this.leaves) {
            // TODO:
        }

        return ret;
    }

    /* tricky methods goes below */

    private int t() {
        return this.tree.getProperties().getDegree();
    }

    protected BTreeNode<V> father() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: father();
    }

    protected BTree<V> predecessor() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: get its idx from father(), if val < 0 even father unless most father is root, else get its idx - 1
    }

    protected BTree<V> successor() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: get its idx from father(), if val > (2 * t) even father unless most father is root, else get its idx + 1
    }
}
