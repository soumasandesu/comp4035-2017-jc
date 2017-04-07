package edu.hkbu.comp4035.y2017.jc;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BTreePrinter {
    public static String doPrintAsString(BTree bTree) {
        return printKeysAsString(bTree);
    }

    private static String printKeysAsString(BTree bTree) {
        LinkedList<String> lines = printKeysFromNodeAsString(Stream.of(bTree.getRootNode()).collect(Collectors.toList()));
        return lines.stream().collect(Collectors.joining("\n"));
    }

    private static LinkedList<String> printKeysFromNodeAsString(Collection<BTreeNode> nodes) {
        boolean leaf = false;
        LinkedList<String> strNodesKeys = new LinkedList<>();
        for (BTreeNode n : nodes) {
            leaf |= n.isLeaf();
            //noinspection unchecked
            String nkStr = "[" + n.getKeys().stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
            strNodesKeys.add(nkStr);
        }

        int strNodesKeysLen = StringUtils.join(strNodesKeys, "").length();

        if (leaf) {
            LinkedList<String> ret = new LinkedList<>();
            ret.add(StringUtils.join(strNodesKeys, " "));
            return ret;
        }

        List<BTreeNode> collect = nodes.stream().flatMap(n -> ((BTreeIndexNode) n).getSubItems().stream()).collect(Collectors.toList());
        LinkedList<String> strings = printKeysFromNodeAsString(collect);

        assert strings != null;
        int strinbBoxWidth = strings.getLast().length();
        int padEach = (int) (1.0 * (strinbBoxWidth - strNodesKeysLen) / (strNodesKeys.size() + 1));
        String thisLineStr = strNodesKeys.stream().map(e -> StringUtils.leftPad("", padEach, " ") + e).collect(Collectors.joining(""));
        strings.push(thisLineStr);

        return strings;
    }
}
