package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;
import java.util.LinkedList;

class BTreeSearchOperations {
    /*
     This method implements range queries. Given a search range ({@code key1}, {@code key2}), the method returns all
     the qualifying key values in the range of between {@code key1} and {@code key2} in the B+-tree. If such keys are
     not found, it returns nothing. <i><b>Be careful with the duplicate keys that span over multiple pages.</b></i>
     */

    static <T> T doSearch(BTree<T> bTree, int key) {
        SearchNodeResult<T> searchResult = searchNodeByKey(bTree.getRootNode(), key);
        //noinspection ConstantConditions
        BTreeLeafNode<T> node = searchResult.getDestinationNode();
        int idx = searchResult.getSearchResultIndex();

        // == key1
        if (key == node.getKeyAt(idx)) {
            return node.getSubItemAt(idx);
        }
        return null;
    }

    static <T> Collection<T> doRangeSearchInclusive(BTree<T> bTree, int key1, int key2) {
        return doRangeSearch(bTree, key1, key2, true);
    }

    static <T> Collection<T> doRangeSearchExclusive(BTree<T> bTree, int key1, int key2) {
        return doRangeSearch(bTree, key1, key2, false);
    }

    private static <T> Collection<T> doRangeSearch(BTree<T> bTree, int key1, int key2, boolean inclusive) {
        LinkedList<T> ret = new LinkedList<>();

        SearchNodeResult<T> dataStart = searchNodeByKey(bTree.getRootNode(), key1);
        {
            //noinspection ConstantConditions
            BTreeLeafNode<T> node = dataStart.getDestinationNode();
            int idx = dataStart.getSearchResultIndex();

            // == key1
            if (inclusive && key1 == node.getKeyAt(idx)) {
                ret.add(node.getSubItemAt(idx));
            }

            // between key1 and key2
            for (++idx; node.getKeyAt(idx) < key2;) {
                ret.add(node.getSubItemAt(idx));

                if (++idx >= node.n()) {
                    idx = 0;
                    node = node.getNextLeaf();
                }
            }

            // == key2
            if (inclusive && key2 == node.getKeyAt(idx)) {
                ret.add(node.getSubItemAt(idx));
            }
        }

        return ret;
    }

    private static <T> SearchNodeResult<T> searchNodeByKey(BTreeNode node, int key) {
        int i = 0;
        while (i < node.n() && key > node.getKeyAt(i)) {
            i++;
        }

        if (node.isLeaf()) {
            // && key == node.getKeyAt(i)
            if (i < node.n()) {
                //noinspection unchecked
                return new SearchNodeResult<>((BTreeLeafNode<T>) node, i);
            } else {
                return null;
            }
        } else {
            return searchNodeByKey((BTreeNode) node.getSubItemAt(i), key);
        }
    }

    final static class SearchNodeResult<T> {
        private final BTreeLeafNode<T> destinationNode;
        private final int searchResultIndex;

        SearchNodeResult(BTreeLeafNode<T> destinationNode, int searchResultIndex) {
            if (destinationNode == null) {
                throw new IllegalArgumentException("destination node cannot be null");
            }
            this.destinationNode = destinationNode;
            this.searchResultIndex = searchResultIndex;
        }

        /**
         * Get the index of the value that possible be equal or (if not match), larger than the desired value.
         *
         * @return The index (>=).
         */
        int getSearchResultIndex() {
            return searchResultIndex;
        }

        BTreeLeafNode<T> getDestinationNode() {
            return destinationNode;
        }
    }

    /*
    enum SearchEqualityOperators {
        // = 1
        // < 2
        // > 4

        UNEQUAL(0),
        EQUAL(1),
        LESS_THAN(2),
        LESS_THAN_OR_EQUAL(3),
        GREATER_THAN(4),
        GREATER_THAN_OR_EQUAL(5);

        public final int val;

        SearchEqualityOperators(int b) {
            if (b < 0 || b > 5)
                throw new IllegalArgumentException("b >= 0 && b <= 5");

            this.val = b;
        }

        public boolean canBeEqual() {
            return (val & EQUAL.val) == EQUAL.val;
        }

        public boolean canBeLessThan() {
            return (val & LESS_THAN.val) == LESS_THAN.val;
        }

        public boolean canBeGreaterThan() {
            return (val & GREATER_THAN.val) == GREATER_THAN.val;
        }
    }
    */
}
