package edu.hkbu.comp4035.y2017.jc;

public class BTreeDeleteOperations {
    /*

    This method deletes an entry (key, rid) from a leaf node. Deletion from a leaf node may cause one or more entries in
    the index node to be deleted. You should always check if a node underflows (less than 50% full) after deletion. If a
    node becomes underflows, merging or redistribution will occur (read and implement the algorithm in the notes).

    You should consider different scenarios separately (maybe write separate functions for them). You should consider
    deletion from a leaf node and index node separately. Deletion from the root should also be considered separately.

    The following code fragment may be helpful:

    Checking if a node is half full:

    if (node->AvailableEntries() < (Fanout-1)/2)
    {
        // Try to re-distribute, borrowing from sibling
        // (adjacent node with same parent as this node).
        // If re-distribution fails, merge this node and sibling.
    }

     */

    // THROW WHENEVER ERROR ENCOUNTERS!!! null IS NOT FOR ERROR THINGS!

    public static void doDelete(BTree bTree, int key) {

    }
}
