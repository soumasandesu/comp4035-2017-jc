package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;

@SuppressWarnings({"Duplicates", "unchecked"})
/**
 * Contains the instructions to performs the insertion in a B+ tree.<br/>
 * Note that this class simulates according to the pseudo code of "Introduction to Algorithms 3rd edition" book.
 */
class BTreeInsertOperations {
    /**
     * Performs the insertion in a B+ tree.
     * @param bTree The tree to insert.
     * @param key The key to insert.
     * @param value The value to insert.
     * @param <V> The value type of B+ tree.
     */
    static <V> void doInsert(BTree<V> bTree, int key, V value) {
        BTreeNode newRoot = insert(bTree.getRootNode(), key, value);
        bTree.setRootNode(newRoot);
    }

    private static <V> BTreeNode insert(BTreeNode root, int key, V value) {
        if (root.isKeysFull()) {
            BTreeIndexNode newRoot = new BTreeIndexNode(root.getTree());
            newRoot.addSubItemValue(root);
            splitChild(newRoot, 0);
            insertNonFull(newRoot, key, value);
            return newRoot;
        } else {
            insertNonFull(root, key, value);
        }
        return root;
    }

    private static <V> void insertNonFull(BTreeNode x, int k, V v) {
        if (!x.getTree().isValueMatchActualType(v)) {
            throw new ClassCastException("!(value instanceof VType)");
        }
        int i = x.n() - 1;
        while (i > -1 && k < x.getKeyAt(i)) {
            i--;
        }
        if (x.isLeaf()) {
            x.addKeyAt(i + 1, k);
            x.addSubItemValueAt(i + 1, v);
        } else {
            i++;
            BTreeNode xc = (BTreeNode) x.getSubItemAt(i);
            if (xc.isKeysFull()) {
                splitChild((BTreeIndexNode) x, i);
                if (k > x.getKeyAt(i)) {
                    xc = (BTreeNode) x.getSubItemAt(++i);
                }
            }
            insertNonFull(xc, k, v);
        }
    }

    private static void splitChild(BTreeIndexNode x, int index) {
        BTreeNode l = x.getSubItemAt(index);
        BTreeNode r = l.getEmptyClone();
        {
            Collection l_rightHalfKeys = l.getKeys(l.t(), l.keysSize());
            r.addKeys(l_rightHalfKeys);
            l.removeKeys(l_rightHalfKeys);
        }
        {
            Collection l_rightHalfSubValues = l.getSubItems(l.t(), l.subItemsSize());
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
