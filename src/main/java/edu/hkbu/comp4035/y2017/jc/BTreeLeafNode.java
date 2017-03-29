package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
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

    boolean isSubItemFull() {
        return this.leavesValues.size() >= 2 * t() - 1;
    }

    public BTreeLeafNode<V> getNextLeaf() {
        return nextLeaf;
    }

    public void setNextLeaf(BTreeLeafNode<V> nextLeaf) {
        this.nextLeaf = nextLeaf;
    }
}
