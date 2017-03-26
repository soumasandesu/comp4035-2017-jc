package edu.hkbu.comp4035.y2017.jc;

// IMMUTABLE
public class BTreeStatistics {
    private final int nodesCount;
    private final int dataEntriesCount;
    private final int indexEntriesCount;
    private final int averageFillFactor;
    private final int height;

    public BTreeStatistics(int nodesCount, int dataEntriesCount, int indexEntriesCount, int averageFillFactor, int height) {
        this.nodesCount = nodesCount;
        this.dataEntriesCount = dataEntriesCount;
        this.indexEntriesCount = indexEntriesCount;
        this.averageFillFactor = averageFillFactor;
        this.height = height;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public int getDataEntriesCount() {
        return dataEntriesCount;
    }

    public int getIndexEntriesCount() {
        return indexEntriesCount;
    }

    public int getAverageFillFactor() {
        return averageFillFactor;
    }

    public int getHeight() {
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
