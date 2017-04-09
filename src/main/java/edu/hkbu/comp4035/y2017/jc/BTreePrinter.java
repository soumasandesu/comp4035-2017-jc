package edu.hkbu.comp4035.y2017.jc;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BTreePrinter {
    public static String doPrintAsString(BTree bTree) {
        return _printKeysAsString(bTree);
    }

    private static String _printKeysAsString(BTree bTree) {
        LinkedList<String> lines = _printKeysFromNodeAsString(Stream.of(bTree.getRootNode()).collect(Collectors.toList()));
        return lines.stream().collect(Collectors.joining("\n"));
    }

    private static LinkedList<String> _printKeysFromNodeAsString(Collection<BTreeNode> nodes) {
        boolean leaf = false;
        LinkedList<String> strNodesKeys = new LinkedList<>();
        for (BTreeNode n : nodes) {
            leaf |= n.isLeaf();
            //noinspection unchecked
            String nkStr = doPrintNodeKeysAsString(n);
            strNodesKeys.add(nkStr);
        }

        int strNodesKeysLen = StringUtils.join(strNodesKeys, "").length();

        if (leaf) {
            LinkedList<String> ret = new LinkedList<>();
            ret.add(StringUtils.join(strNodesKeys, " "));
            return ret;
        }

        List<BTreeNode> collect = nodes.stream().flatMap(n -> ((BTreeIndexNode) n).getSubItems().stream()).collect(Collectors.toList());
        LinkedList<String> strings = _printKeysFromNodeAsString(collect);

        assert strings != null;
        int strinbBoxWidth = strings.getLast().length();
        int padEach = (int) (1.0 * (strinbBoxWidth - strNodesKeysLen) / (strNodesKeys.size() + 1));
        String thisLineStr = strNodesKeys.stream().map(e -> StringUtils.leftPad("", padEach, " ") + e).collect(Collectors.joining(""));
        strings.push(thisLineStr);

        return strings;
    }

    public static String doPrintNodeKeysAsString(BTreeNode n) {
        //noinspection unchecked
        return "[" + n.getKeys().stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
    }

    static String doPrintNodeAsString(BTreeNode node) {
        String hash = Integer.toHexString(node.hashCode());
        //noinspection unchecked
        String keys = doPrintNodeKeysAsString(node);
        String subItems = "[\n %s\n]";
        if (node.isLeaf()) {
            //noinspection unchecked
            subItems = String.format(subItems, node.getSubItems().stream()
                    .map(n -> n == null ? "null" : n.toString())
                    .collect(Collectors.joining(",")));
        } else {
            subItems = String.format(subItems, ((BTreeIndexNode)node).getSubItems().stream()
                    .map(BTreePrinter::doPrintNodeKeysAsString)
                    .collect(Collectors.joining(",\n ")));
        }
        int usedSpace = node.usedSpace();
        int totalSpace = node.totalSpace();

        return String.format("Node %s@%s:\nKeys: %s\nSub-Items: %s\nUsed Space: %d\nTotal Space: %d\nAverage Fill Factor: %.2f%%",
                doPrintNodeFriendlyNameAsString(node), hash, keys, subItems, usedSpace, totalSpace, 100.0 * usedSpace / totalSpace);
    }

    public static String doPrintNodeFriendlyNameAsString(BTreeNode node) {
        return String.format("BTree%sNode<%s>", node.isLeaf() ? "Leaf" : "Index", node.getTree().getValueType().getSimpleName());
    }
}
