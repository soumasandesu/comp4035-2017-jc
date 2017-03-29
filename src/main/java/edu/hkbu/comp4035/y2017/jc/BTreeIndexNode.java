package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class BTreeIndexNode extends BTreeNode<BTreeNode> {
    private final Vector<BTreeNode> subNodes;

    BTreeIndexNode(BTree tree) {
        super(tree);
        subNodes = new Vector<>();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    BTreeIndexNode getEmptyClone() {
        return new BTreeIndexNode(this.getTree());
    }

    @Override
    BTreeNode getSubItemAt(int index) {
        return subNodes.get(index);
    }

    @Override
    Collection<BTreeNode> getSubItems() {
        //noinspection unchecked
        return (List<BTreeNode>) subNodes.clone();
    }

    @Override
    Collection<BTreeNode> getSubItems(int start_inclusive, int end_exclusive) {
        return subNodes.subList(start_inclusive, end_exclusive);
    }

    @Override
    boolean removeSubItemValue(BTreeNode bTreeIndexNode) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            ok = subNodes.removeElement(bTreeIndexNode);
        }
        return ok;
    }

    @Override
    boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry();
        if (ok) {
            subNodes.removeElementAt(index);
        }
        return ok;
    }

    boolean isSubItemsFull() {
        return this.subNodes.size() >= 2 * t();
    }

    @Override
    boolean isSubItemsHungry() {
        return this.subNodes.size() < t();
    }
}
