package edu.hkbu.comp4035.y2017.jc;

public class BTreeInsertOperations {
    /*
    This method inserts a pair (key, rid) into the B+-Tree Index (rid can always be assumed to be 0 in your implementation).
    The actual pair (key, rid) is inserted into a leaf node. But this insertion may cause one or more (key, pid) pair to be
    inserted into index nodes. You should always check to see if the current node has enough space before you insert. If you
    don't have enough space, you have to split the current node by creating a new node, and copy some of the data over from
    the current node to the new node.1 Splitting will cause a new entry to be added in the parent node.

    Splitting of the root node should be considered separately, since if we have a new root, we need to update the root
    pointer to reflect the changes. Splitting of a leaf node should also be considered separately since the leaf nodes are
    linked as a link list.

    Due to the complexity of this function, we recommend that you write separate functions for different cases. For example,
    it is a good idea to write a function to insert into a leaf node, and a function to insert into an index node.
     */
}
