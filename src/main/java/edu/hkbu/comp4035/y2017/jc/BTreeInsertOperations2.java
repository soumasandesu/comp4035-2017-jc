package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;

@SuppressWarnings({"Duplicates", "unchecked"})
/**
 * Contains the instructions to performs the insertion in a B+ tree.<br/>
 * Note that this class simulates according to the "B+ tree visualisation" by University of San Francisco available on
 * {@link https://www.cs.usfca.edu/~galles/visualization/BPlusTree.html}.
 */
class BTreeInsertOperations2 {
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
            insertNonRoot(newRoot, key, value); // still can insert, de-iterate index will become 0
//            splitChild(newRoot, 0); // web example says to split after insertNonRoot
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
            if (xc.isSubItemsFull()) {
                splitChild((BTreeIndexNode) x, i);
                insertNonRoot(x, k, v); // re-calculate for choosing correct side
                balance((BTreeIndexNode) x, i);
            } else {
                insertNonRoot(xc, k, v);
            }
        }
    }

    private static void splitChild(BTreeIndexNode x, int index) {
        BTreeNode l = x.getSubItemAt(index);
        BTreeNode r = l.getEmptyClone();
        {
            int count_keys = ((int)Math.ceil(l.keysSize() / 2.0));
            Collection l_rightHalfKeys = l.getKeys(l.keysSize() - count_keys, l.keysSize());
            r.addKeys(l_rightHalfKeys);
            l.removeKeys(l_rightHalfKeys);
        }
        {
            int count_subItems = r.keysSize();
            Collection l_rightHalfSubValues = l.getSubItems(l.subItemsSize() - count_subItems, l.subItemsSize());
            r.addSubItemValues(l_rightHalfSubValues);
            l.removeSubItemsValues(l_rightHalfSubValues);
        }
        x.addSubItemValueAt(index + 1, r);
        x.addKeyAt(index, r.getKeyAt(0));
        if (r.isLeaf()) {
            ((BTreeLeafNode) l).setNextLeaf((BTreeLeafNode) r);
        } else {
            assert r.keysSize() > 1;
            r.removeKeyAt(0);
        }
    }

    private static void balance(BTreeIndexNode parent, int index) {
        BTreeNode l = parent.getSubItemAt(index);
        BTreeNode r = parent.getSubItemAt(index + 1);

        int diff = l.keysSize() - r.keysSize();
        if (diff < -1) {
            int rm_r = Math.abs(diff) - 1;
            Collection<Integer> keys = r.getKeys(0, rm_r);
            Collection<BTreeNode> values = r.getSubItems(0, rm_r);
            if (!r.isLeaf()) {
                int pK = parent.getKeyAt(index);
                l.addKeyAt(l.keysSize(), pK);
                parent.removeKeyAt(index);
            }
            l.addKeys(keys);
            r.removeKeys(keys);
            l.addSubItemValues(values);
            r.removeSubItemsValues(values);
            if (!l.isLeaf()) {
                int npk = l.getKeyAt(l.keysSize() - 1);
                parent.addKeyAt(index, npk);
                l.removeKeyAt(l.keysSize() - 1);
            } else {
                parent.addKeyAt(index + 1, r.getKeyAt(0));
                parent.removeKeyAt(index);
            }
        } else if (diff > 1) {
            int rm_l = Math.abs(diff) - 1;
            Collection<Integer> keys = l.getKeys(l.keysSize() - rm_l, l.keysSize());
            Collection<BTreeNode> values = l.getSubItems(l.keysSize() - rm_l, l.keysSize());
            if (!r.isLeaf()) {
                int pK = parent.getKeyAt(index);
                r.addKeyAt(0, pK);
                parent.removeKeyAt(index);
            }
            keys.forEach(k -> r.addKeyAt(0, k));
            l.removeKeys(keys);
            values.forEach(v -> r.addSubItemValueAt(0, v));
            l.removeSubItemsValues(values);
            if (!r.isLeaf()) {
                int npk = r.getKeyAt(0);
                parent.addKeyAt(parent.keysSize(), npk);
                r.removeKeyAt(0);
            } else {
                parent.addKeyAt(index + 1, l.getKeyAt(l.keysSize() - 1));
                parent.removeKeyAt(index);
            }
        }
    }
}
