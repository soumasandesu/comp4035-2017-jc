package edu.hkbu.comp4035.y2017.jc;

/**
 * Store the information about the properties about the related BTree.
 */
/*
   All of the properties should be immutable.
 */
final class BTreeProperties {
    // TODO: obj ref to the root
    private final int degree;

    public BTreeProperties(int degree) {
        // degree
        if (degree < 2) throw new IllegalArgumentException("degree for B+Tree cannot be less than 2.");
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }
}
