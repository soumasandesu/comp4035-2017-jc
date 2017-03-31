package edu.hkbu.comp4035.y2017.jc;

public class BTreeSearchOperations {
    /*
     This method implements range queries. Given a search range ({@code key1}, {@code key2}), the method returns all
     the qualifying key values in the range of between {@code key1} and {@code key2} in the B+-tree. If such keys are
     not found, it returns nothing. <i><b>Be careful with the duplicate keys that span over multiple pages.</b></i>
     */

    public static <T> T doSearch(BTree<T> bTree, int key) {
        //noinspection unchecked
        return (T) search(bTree.getRootNode(), key);
    }

    private static Object search(BTreeNode node, int key) {
        int i = 0;
        while (i < node.n() && key > node.getKeyAt(i)) {
            i++;
        }

        if (node.isLeaf()) {
            if (i < node.n() && key == node.getKeyAt(i)) {
                return node.getSubItemAt(i);
            }
            return null;
        } else {
            return search((BTreeNode) node.getSubItemAt(i), key);
        }
    }
}
