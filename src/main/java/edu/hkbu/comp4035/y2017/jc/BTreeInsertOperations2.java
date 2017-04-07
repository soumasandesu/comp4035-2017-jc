package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;

@SuppressWarnings({"Duplicates", "unchecked"})
class BTreeInsertOperations2 {
    static <V> void doInsert(BTree<V> bTree, int key, V value) {
        BTreeNode newRoot = insert(bTree.getRootNode(), key, value);
        bTree.setRootNode(newRoot);
    }

    private static <V> BTreeNode insert(BTreeNode root, int key, V value) {
        if (root.isKeysFull()) {
            BTreeIndexNode newRoot = new BTreeIndexNode(root.getTree());
            newRoot.addSubItemValue(root);
            insertNonRoot(newRoot, key, value); // still can insert, de-iterate index will become 0
            splitChild(newRoot, 0); // web example says to split after insertNonRoot
            return newRoot;
        } else {
            insertNonRoot(root, key, value);
        }

        return root;
    }

    private static <V> void insertNonRoot(BTreeNode x, int k, V v) {
        if (!x.getTree().isValueMatchActualType(v)) {
            throw new ClassCastException("!(value instanceof VType)");
        }
        int i = x.n() - 1;
        while (i > -1 && k < x.getKeyAt(i)) {
            i--;
        }
        ++i;
        if (x.isLeaf()) {
            x.addKeyAtBypass(i, k);
            x.addSubItemValueAtBypass(i, v);
        } else {
            BTreeNode xc = (BTreeNode) x.getSubItemAt(i);
            insertNonRoot(xc, k, v);
            if (xc.isKeysFull()) {
                splitChild((BTreeIndexNode) x, i);
            }
        }
    }

    private static void splitChild(BTreeIndexNode x, int index) {
        BTreeNode l = x.getSubItemAt(index);
        BTreeNode r = l.getEmptyClone();
        {
            Collection l_rightHalfKeys = l.getKeys(l.t(), (l.t() * 2) - 1);  // deep clone
            r.addKeys(l_rightHalfKeys);
            l.removeKeys(l_rightHalfKeys);
        }
        {
            int endPosExclusive = l.isLeaf() ? l.t() * 2 : l.t() * 2 + 1;

            Collection l_rightHalfSubValues = l.getSubItems(l.t(), endPosExclusive - 1);  // deep clone
            r.addSubItemValues(l_rightHalfSubValues);
            l.removeSubItemsValues(l_rightHalfSubValues);
        }
        x.addSubItemValueAt(index + 1, r);
        x.addKeyAt(index, r.getKeyAt(0));
        if (!r.isLeaf()) {
            r.removeKeyAt(0);
        }
    }
}
