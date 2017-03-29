package edu.hkbu.comp4035.y2017.jc;

import java.util.Vector;

public class BTreeIndexNode extends BTreeNode<BTreeIndexNode> {
    private final Vector<BTreeIndexNode> subNodes;

    BTreeIndexNode(BTree tree) {
        super(tree);
        subNodes = new Vector<>(t());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    boolean isSubItemFull() {
        return this.subNodes.size() >= 2 * t();
    }
}
