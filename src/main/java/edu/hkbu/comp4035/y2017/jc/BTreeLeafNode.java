package edu.hkbu.comp4035.y2017.jc;

import java.util.Vector;

public class BTreeLeafNode<V> extends BTreeNode {
    private final Vector<V> leavesValues;
    private BTreeLeafNode<V> nextLeaf;

    BTreeLeafNode(BTree tree) {
        super(tree);
        this.leavesValues = new Vector<V>(t() - 1);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    public boolean addLeaf(int key, V value) {
        boolean ok = addKey(key);
        if (ok) {
            ok = addLeafValue(value);
        }
        return ok;
    }

    private boolean addLeafValue(V value) {
        boolean ok = !isLeavesFull();
        if (ok) {
            ok = leavesValues.add(value);
        }
        return ok;
    }

    private boolean isLeavesFull() {
        return this.leavesValues.size() >= t() - 1;
    }

    public BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    public void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
