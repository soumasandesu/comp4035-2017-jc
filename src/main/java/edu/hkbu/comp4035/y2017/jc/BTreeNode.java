package edu.hkbu.comp4035.y2017.jc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: javadocs for BTreeNode<T>
public class BTreeNode<VType> implements Serializable {
    private final BTree tree;
    private final ArrayList<BTreeNode<VType>> leaves;
    private final HashMap<Integer, VType> keys;

    public BTreeNode(BTree tree) {
        this.tree = tree;
        this.leaves = new ArrayList<>(2 * t());
        this.keys = new HashMap<>(2 * t() - 1);
    }

    protected BTreeNode(BTree tree, ArrayList<BTreeNode<VType>> leaves, HashMap<Integer, VType> keys) {
        this.tree = tree;
        this.leaves = leaves;
        this.keys = keys;
    }

    public BTreeNode<VType> getLeaveAt(int index) {
        return this.leaves.get(index);
    }

    public VType getValueAtKey(int index) {
        return this.keys.get(index);
    }

    public boolean hasKey(int index) {
        return this.keys.containsKey(index);
    }

    public boolean isLeaf() {
        return leaves.size() > 0;
    }

    private int t() {
        return this.tree.getProperties().getDegree();
    }

    protected BTreeNode<VType> father() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: father();
    }

    protected BTree<VType> predecessor() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: get its idx from father(), if val < 0 even father unless most father is root, else get its idx - 1
    }

    protected BTree<VType> successor() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: get its idx from father(), if val > (2 * t) even father unless most father is root, else get its idx + 1
    }
}
