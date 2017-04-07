package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;

@SuppressWarnings("Duplicates")
class BTreeInsertOperations2 {
    /*
    This method inserts a pair (key, rid) into the B+-Tree Index (rid can always be assumed to be 0 in your implementation).
    The actual pair (key, rid) is inserted into a leaf node. But this insertion may cause one or more (key, pid) pair to be
    inserted into index nodes. You should always check to see if the current node has enough space before you insertChooseBypass. If you
    don't have enough space, you have to splitChild the current node by creating a new node, and copy some of the data over from
    the current node to the new node.1 Splitting will cause a new entry to be added in the parent node.

    Splitting of the root node should be considered separately, since if we have a new root, we need to update the root
    pointer to reflect the changes. Splitting of a leaf node should also be considered separately since the leaf nodes are
    linked as a link list.

    Due to the complexity of this function, we recommend that you write separate functions for different cases. For example,
    it is a good idea to write a function to insertChooseBypass into a leaf node, and a function to insertChooseBypass into an index node.
     */

    // THROW WHENEVER ERROR ENCOUNTERS!!! null IS NOT FOR ERROR THINGS!


    static <V> void doInsert(BTree<V> bTree, int key, V value) {
        BTreeNode newRoot = insert(bTree.getRootNode(), key, value);
        bTree.setRootNode(newRoot);
    }

    private static <V> BTreeNode insert(BTreeNode root, int key, V value) {
        if (root.isKeysFull()) {  // r.n == 2t - 1
            // s = ALLOCATE-NODE()
            // s.leaf = FALSE
            // s.n = 0
            // s.c[1] = r
            // B-TREE-SPLIT-CHILD(s, 1)
            // B-TREE-INSERT-NON-FULL(s, k)
            // T.root = s

            BTreeIndexNode newRoot = new BTreeIndexNode(root.getTree());
            newRoot.addSubItemValue(root);
            insertChooseBypass(newRoot, key, value, true);
            splitChild(newRoot, 0); // web example says to split after insertChooseBypass
            return newRoot;
        } else {
            insertNonFull(root, key, value);
        }

        return root;
    }

    private static <V> void insertNonFull(BTreeNode x, int k, V v) {
        insertChooseBypass(x, k, v, false);
    }

    private static <V> void insertChooseBypass(BTreeNode x, int k, V v, boolean bypass) {
        if (!x.getTree().isValueMatchActualType(v)) {
            throw new ClassCastException("!(value instanceof VType)");
        }

        int i = x.n() - 1;
        while (i > -1 && k < x.getKeyAt(i)) {
            i--;
        }
        if (x.isLeaf()) {
            x.addKeyAt(i + 1, k, bypass);
            //noinspection unchecked
            x.addSubItemValueAt(i + 1, v, bypass);
        } else {
            i++;
            BTreeNode xc = (BTreeNode) x.getSubItemAt(i);
            if (xc.isKeysFull() && !bypass) {
                splitChild((BTreeIndexNode) x, i);
                if (k > x.getKeyAt(i)) {
                    xc = (BTreeNode) x.getSubItemAt(++i);
                }
            }
            insertChooseBypass(xc, k, v, bypass);
        }
    }

    private static void splitChild(BTreeIndexNode x, int index) {
        // z = AllocateNode()
        // y = x.c[i]
        // z.leaf = y.leaf
        BTreeNode l = x.getSubItemAt(index);
        BTreeNode r = l.getEmptyClone();

        // z.n = t - 1

        // for j = 1 to t-1
        //     z.key[j] = y.key[j+t]
        {
            Collection l_rightHalfKeys = l.getKeys(l.t(), (l.t() * 2) - 1);  // deep clone
            //noinspection unchecked
            r.addKeys(l_rightHalfKeys);
            //noinspection unchecked
            l.removeKeys(l_rightHalfKeys);
        }

        // if not y.leaf
        //     for j = 1 to t
        //         z.c[j] = y.c[j+t]
        {
            int endPosExclusive = l.isLeaf() ? l.t() * 2 : l.t() * 2 + 1;

            Collection l_rightHalfSubValues = l.getSubItems(l.t(), endPosExclusive - 1);  // deep clone
            //noinspection unchecked
            r.addSubItemValues(l_rightHalfSubValues);
            //noinspection unchecked
            l.removeSubItemsValues(l_rightHalfSubValues);
        }

        // y.n = t - 1

        // for j = x.n+1 downto i+1
        //     x.c[j+1] = x.c[j]
        // x.c[i+1] = z
        x.addSubItemValueAt(index + 1, r);

        // for j = x.n downto i
        //     x.key[j+1] = x.key[j]
        // x.key[i] = y.key[t]
        x.addKeyAt(index, r.getKeyAt(0));
        if (!r.isLeaf()) {
            r.removeKeyAt(0);
        }

        // DISK-WRITE(y)
        // DISK-WRITE(z)
        // DISK-WRITE(x)
    }
}
