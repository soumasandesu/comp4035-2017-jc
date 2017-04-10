package edu.hkbu.comp4035.y2017.jc;

import java.util.stream.Collectors;

public class BTreeStatValidator {
    static boolean doValidate(BTree bTree) {
        return _validate(bTree.getRootNode());
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean _validate(BTreeNode n) {
        if (!n.isValid()) {
            return false;
        }

        if (!n.isLeaf()) {
            for (boolean b : ((BTreeIndexNode) n).getSubItems().stream()
                    .map(BTreeStatValidator::_validate)
                    .collect(Collectors.toList())) {
                if (!b) return false;
            }
        }
        return true;
    }

    static int doCountNodes(BTree bTree) {
        return _countNodes(bTree.getRootNode()) + 1;
    }

    private static int _countNodes(BTreeNode n) {
        if (n.isLeaf()) {
            return 0; // root already counts
        } else {
            BTreeIndexNode in = (BTreeIndexNode) n;
            if (in.getSubItemAt(0).isLeaf()) {
                return in.subItemsSize();
            } else {
                return in.getSubItems().stream().mapToInt(BTreeStatValidator::_countNodes).sum() + in.subItemsSize();
            }
        }
    }

    static int doCountIndex(BTree bTree) {
        return _countIndex(bTree.getRootNode());
    }

    private static int _countIndex(BTreeNode n) {
        if (n.isLeaf()) {
            return n.keysSize();
        } else {
            return ((BTreeIndexNode) n).getSubItems().stream().mapToInt(BTreeStatValidator::_countIndex).sum() + n.subItemsSize();
        }
    }

    static double doCountAverageFillFactorPercent(BTree bTree) {
        return 100.0 * _countUsedSpace(bTree.getRootNode()) / _countTotalSpace(bTree.getRootNode());
    }

    private static int _countTotalSpace(BTreeNode n) {
        if (n.isLeaf()) {
            return n.totalSpace();
        } else {
            return ((BTreeIndexNode)n).getSubItems().stream().mapToInt(BTreeStatValidator::_countTotalSpace).sum() + n.totalSpace();
        }
    }

    private static int _countUsedSpace(BTreeNode n) {
        if (n.isLeaf()) {
            return n.usedSpace();
        } else {
            return ((BTreeIndexNode)n).getSubItems().stream().mapToInt(BTreeStatValidator::_countUsedSpace).sum() + n.usedSpace();
        }
    }

    static int doCountData(BTree bTree) {
        return _countData(bTree.getRootNode());
    }

    private static int _countData(BTreeNode n) {
        if (n == null) {
            return 0;
        } else if (n.isLeaf()) {
            return n.subItemsSize() + _countData(((BTreeLeafNode)n).getNextLeaf());
        } else {
            return _countData(((BTreeIndexNode)n).getSubItemAt(0));
        }
    }

    static int doMeasureHeight(BTree bTree) {
        int i = 0;
        for (BTreeNode n = bTree.getRootNode(); n != null && !n.isLeaf(); n = ((BTreeIndexNode)n).getSubItemAt(0)) {
            ++i;
        }
        return i;
    }

    static Statistics generate(BTree bTree) {
        return new Statistics(doCountNodes(bTree), doCountData(bTree), doCountIndex(bTree), doCountAverageFillFactorPercent(bTree), doMeasureHeight(bTree));
    }

    // IMMUTABLE
    static class Statistics {
        private final int nodesCount;
        private final int dataEntriesCount;
        private final int indexEntriesCount;
        private final double averageFillFactor;
        private final int height;

        private Statistics(int nodesCount, int dataEntriesCount, int indexEntriesCount, double averageFillFactor, int height) {
            this.nodesCount = nodesCount;
            this.dataEntriesCount = dataEntriesCount;
            this.indexEntriesCount = indexEntriesCount;
            this.averageFillFactor = averageFillFactor;
            this.height = height;
        }

        private int getNodesCount() {
            return nodesCount;
        }

        private int getDataEntriesCount() {
            return dataEntriesCount;
        }

        private int getIndexEntriesCount() {
            return indexEntriesCount;
        }

        private double getAverageFillFactor() {
            return averageFillFactor;
        }

        private int getHeight() {
            return height;
        }

        @Override
        public String toString() {
            String msg = "";
            msg += "Statistics of the B+-tree:";
            msg += "    Total number of nodes: %d";
            msg += "    Total number of data entries: %d";
            msg += "    Total number of index entries: %d";
            msg += "    Average fill factor of leaf nodes: %d%%";
            msg += "    Height of tree: %d";
            return String.format(msg,
                    getNodesCount(),
                    getDataEntriesCount(),
                    getIndexEntriesCount(),
                    getAverageFillFactor(),
                    getHeight());
        }
    }
}
