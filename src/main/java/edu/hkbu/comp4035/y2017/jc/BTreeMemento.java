package edu.hkbu.comp4035.y2017.jc;

public class BTreeMemento {


    class MementoData {
        private final BTreeNode root;

        MementoData(BTreeNode root) {
            this.root = root;
        }

        public BTreeNode getRoot() {
            return root;
        }
    }
}
