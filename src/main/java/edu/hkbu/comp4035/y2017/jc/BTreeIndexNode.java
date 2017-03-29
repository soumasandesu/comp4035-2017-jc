package edu.hkbu.comp4035.y2017.jc;

import java.util.Vector;

public class BTreeIndexNode extends BTreeNode {
    private final Vector<BTreeIndexNode> subNodes;

    BTreeIndexNode(BTree tree) {
        super(tree);
        subNodes = new Vector<>(t());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public boolean addSubNode(int key, BTreeIndexNode subNode) {
        boolean ok = addKey(key);
        if (ok) {
            ok = addSubNodeValue(subNode);
        }
        return ok;
    }

    private boolean addSubNodeValue(BTreeIndexNode subNode) {
        boolean ok = !isSubNodesFull();
        if (ok) {
            ok = subNodes.add(subNode);
        }
        return ok;
    }


    private boolean isSubNodesFull() {
        return this.subNodes.size() >= t();
    }
}
