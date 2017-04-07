package edu.hkbu.comp4035.y2017.jc;

class BTreeDeleteOperations {
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

    // Jacky, you may change all those names and signatures of all following methods.
    // I'm just plotting the determination of this class for your reference, according to the textbook. -- Charles

    static void doDelete(BTree bTree, int key) {
        // search position
        // get parent-1
        // check size if hungry
        //     check right won't hungry
        //         borrow one from right
        //         write new index @ parent of borrowed
        //     else
        //         merge
        //     while parent is not null
        //         check upper won't hungry
    }

    private static void checkHungryOrMerge(BTreeIndexNode indexNode, int pos) {

    }

    private static void merge(BTreeIndexNode indexNode, int posProcessing) {

    }
}
