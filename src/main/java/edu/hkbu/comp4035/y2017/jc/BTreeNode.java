package edu.hkbu.comp4035.y2017.jc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/*
    All of the `BTreeNodes` should only have the pointer of its child. The operation to get parent should ... not get,
    but should have already been recorded by `BTree*Operations` whilst traversing through different levels.
 */
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = BTreeIndexNode.class, name = "BTreeIndexNode"),
//        @JsonSubTypes.Type(value = BTreeLeafNode.class, name = "BTreeLeafNode")
//})

/**
 * Represents a node in the B+ Tree.
 * @param <S> The sub-item value type of this node.
 */
abstract class BTreeNode<S> implements Serializable {
    /**
     * The sub-items collection of this node.
     */
    final LinkedList<S> subItems;
    /**
     * The respective B+ Tree that contains this node.
     */
    private final BTree tree;
    /**
     * The keys collection of this node.
     */
    private final LinkedList<Integer> keys;

    /**
     * Creates a new B+ Tree node in a B+ Tree.
     *
     * @param tree The B+ tree.
     */
    BTreeNode(BTree tree) {
        this.tree = tree;
        keys = new LinkedList<>();
        subItems = new LinkedList<>();
    }

    /**
     * Creates a new B+ Tree node in a B+ Tree with pre-stored keys and sub-items. Used by import/export tools.
     *
     * @param tree     The B+ tree.
     * @param keys     The {@code LinkedList} of integers of keys.
     * @param subItems The {@code LinkedList} of {@code S}es of sub-items.
     */
    BTreeNode(BTree tree, LinkedList<Integer> keys, LinkedList<S> subItems) {
        this.tree = tree;
        this.keys = keys;
        this.subItems = subItems;
    }

    private boolean addKey(int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            ok = this.keys.add(key);
        }
        return ok;
    }

    /**
     * Adds key into B+ tree at specific position.
     *
     * @param index The position that the key will be at.
     * @param key   The key to store.
     * @return If store is success.
     */
    final boolean addKeyAt(int index, int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            this.addKeyAtBypass(index, key);
        }
        return ok;
    }

    /**
     * Adds key into B+ tree at specific position.
     *
     * @param index  The position that the key will be at.
     * @param key    The key to store.
     * @param bypass Should the operation bypass the up/lower bounds of sizes of keys collection.
     * @return If store is success.
     */
    final boolean addKeyAt(int index, int key, boolean bypass) {
        if (bypass) {
            addKeyAtBypass(index, key);
            return true;
        } else {
            return addKeyAt(index, key);
        }
    }

    /**
     * Adds key into B+ tree at specific position, with ignoring the up/lower bounds of sizes of keys collection.
     *
     * @param index The position that the key will be at.
     * @param key   The key to store.
     * @return If store is success.
     */
    final void addKeyAtBypass(int index, int key) {
        this.keys.add(index, key);
    }


    /**
     * Adds keys into B+ tree.
     *
     * @param keys Collection of keys.
     * @return If store is success.
     */
    final boolean addKeys(Collection<Integer> keys) {
        boolean ok;
        for (Integer key : keys) {
            ok = addKey(key);
            if (!ok) return false;
        }
        return true;
    }

    /***
     * Adds a sub-item into sub-items collection.
     * @param s The sub-item to store.
     * @return If store is success.
     */
    final boolean addSubItemValue(S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            ok = subItems.add(s);
        }
        return ok;
    }

    /***
     * Adds a sub-item into sub-items collection at specific position.
     * @param index The position that sub-item storing will be at.
     * @param s The sub-item to store.
     * @return If store is success.
     */
    final boolean addSubItemValueAt(int index, S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            addSubItemValueAtBypass(index, s);
        }
        return ok;
    }

    /***
     * Adds a sub-item into sub-items collection at specific position.
     * @param index The position that sub-item storing will be at.
     * @param s The sub-item to store.
     * @param bypass Should the operation bypass the up/lower bounds of sizes of keys collection.
     * @return If store is success.
     */
    final boolean addSubItemValueAt(int index, S s, boolean bypass) {
        if (bypass) {
            addSubItemValueAtBypass(index, s);
            return true;
        } else {
            return addSubItemValueAt(index, s);
        }
    }


    /***
     * Adds a sub-item into sub-items collection at specific position, with ignoring the up/lower bounds of sizes of
     * keys collection.
     * @param index The position that sub-item storing will be at.
     * @param s The sub-item to store.
     * @return If store is success.
     */
    final void addSubItemValueAtBypass(int index, S s) {
        subItems.add(index, s);
    }

    /**
     * Adds a collection of {@code S}es into sub-items collection.
     * @param ss The {@code S}es.
     * @return If store is success.
     */
    final boolean addSubItemValues(Collection<S> ss) {
        boolean ok = true;
        for (S s : ss) {
            ok = this.addSubItemValue(s);
        }
        return ok;
    }

    private BTreeNode<S> deepClone() {
        BTreeNode<S> clone = getEmptyClone();

        // deep clone keys
        keys.stream()
                .map(Integer::new)
                .forEach(clone.keys::add);

        // deep clone subItems
        // case if value are btreenode
        // case if value are clonable
        // case if value are void
        if (subItems.size() > 0) {
            S test = getSubItemAt(0);

            if (test instanceof BTreeNode) {
                //noinspection unchecked
                subItems.stream()
                        .map(s -> ((BTreeNode) s).deepClone())
                        .forEach(s -> clone.subItems.add((S) s));
            } else {
                /*
                Do a shallow copy for following reasons:
                a)  If the data type for values are primitive types, they're immutable that values can't actually be
                    changed
                b)  If the data type is object-like, deep copying object may cause much of overhead that won't make it
                    efficient.

                -- Charles
                 */
                //noinspection unchecked
                clone.subItems.addAll((Collection<? extends S>) subItems.clone());
            }
        }

        return clone;
    }

    /**
     * To create another instance of B+ tree which has nothing in keys and sub-items.
     * @return The new instance of same node with nothing in it.
     */
    abstract BTreeNode<S> getEmptyClone();

    /**
     * Get a key at specific position.
     * @param index The position to get.
     * @return The key at position of {@code index}.
     */
    final int getKeyAt(int index) {
        return keys.get(index);
    }

    /**
     * Get the keys in this node.
     * @return Keys.
     */
    final Collection<Integer> getKeys() {
        //noinspection unchecked
        return (Collection<Integer>) keys.clone();
    }

    /**
     * Get the keys starting from {@code start_inclusive} till {@code end_exclusive}, but except exact the value of
     * {@code end_exclusive}.
     * @param start_inclusive Start position inclusively.
     * @param end_exclusive End position exclusively.
     * @return The keys between specified section.
     */
    final Collection<Integer> getKeys(int start_inclusive, int end_exclusive) {
        return keys.subList(start_inclusive, end_exclusive);
    }

    /**
     * Get a sub-item at specific positon.
     * @param index The index of sub-item to get.
     * @return The item.
     */
    final S getSubItemAt(int index) {
        return subItems.get(index);
    }

    /**
     * Get the sub-items.
     * @return The sub-items.
     */
    final Collection<S> getSubItems() {
        //noinspection unchecked
        return (List<S>) subItems.clone();
    }

    /**
     * Get the sub-items starting from {@code start_inclusive} till {@code end_exclusive}, but except exact the value of
     * {@code end_exclusive}.
     * @param start_inclusive Start position inclusively.
     * @param end_exclusive End position exclusively.
     * @return The sub-items between specified section.
     */
    final Collection<S> getSubItems(int start_inclusive, int end_exclusive) {
        return subItems.subList(start_inclusive, end_exclusive);
    }

    private Class<S> getSubItemsValuesClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            //noinspection unchecked
            return (Class<S>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

    /**
     * Gets the respective B+ tree.
     * @return The B+ tree.
     */
    public final BTree getTree() {
        return tree;
    }

    /**
     * Shows whether the keys are full.
     * @return If keys are full.
     */
    final boolean isKeysFull() {
        // max deg of keys = 2t - 1
        return n() >= 2 * t() - 1;
    }

    /**
     * Shows whether the keys are hungry.
     * @return If keys are hungry.
     */
    final boolean isKeysHungry() {
        // Every node other than the root must have at least t - 1 keys.
        // min deg of keys = t - 1
        return getTree().getRootNode() != this && n() < t() - 1;
    }

    /**
     * Shows whether this node is a leaf node.
     * @return If this node is a leaf node.
     */
    public abstract boolean isLeaf();

    /**
     * Shows whether the sub-items are full.
     * @return If sub-items are full.
     */
    abstract boolean isSubItemsFull();

    /**
     * Shows whether the sub-items are hungry.
     * @return If sub-items are hungry.
     */
    abstract boolean isSubItemsHungry();
    
    abstract boolean isSubItemsHungry(int sizeAdd);

    @SuppressWarnings("ConstantConditions")
    /**
     * Check whether this node is a valid node regarding to the rules of a B+ tree node.
     * @return The result of validation.
     */
    boolean isValid() {
        if (this.isKeysFull()) {
            System.out.printf("%s: keys full", Integer.toHexString(hashCode()));
            return false;
        }
        if (this.isKeysHungry()) {
            System.out.printf("%s: keys hungry", Integer.toHexString(hashCode()));
            return false;
        }
        if (this.isSubItemsFull()) {
            System.out.printf("%s: subitems full", Integer.toHexString(hashCode()));
            return false;
        }
        if (this.isSubItemsHungry()) {
            System.out.printf("%s: subitems hungry", Integer.toHexString(hashCode()));
            return false;
        }
        if (this.isSubItemsHungry()) {
            System.out.printf("%s: key subitems size not ok", Integer.toHexString(hashCode()), this.keys.size());
            return false;
        }
        if (this.keysSize() != this.subItemsSize() + (isLeaf() ? 0 : 1)) {
            System.out.printf("%s: key subitems size not ok (%d, %d)", Integer.toHexString(hashCode()), this.keysSize(), this.subItemsSize());
            return false;
        }
        return true;
    }

    /**
     * Gets the counts of keys.
     * @return How many keys are in this node.
     */
    final int keysSize() {
        return this.keys.size();
    }

    /**
     * Gets the counts of keys.
     * @return How many keys are in this node.
     */
    final int n() {
        return this.keys.size();
    }

    /**
     * Remove a sub-item from the node.
     * @param key The sub-item to remove.
     * @return If the deletion is success.
     */
    private boolean removeKey(int key) {
        boolean ok = !isKeysHungry() && keys.contains(key);
        if (ok) {
            // can't get object reference, so... hack
            keys.remove(keys.indexOf(key));
        }
        return ok;
    }

    /**
     * Remove a sub-item from the node at specific position.
     * @param index The position of sub-item to remove.
     * @return If the deletion is success.
     */
    final boolean removeKeyAt(int index) {
        boolean ok = !isKeysHungry() && keys.size() > index;
        if (ok) {
            removeKeyAtBypass(index);
        }
        return ok;
    }

    /**
     * Remove a key from the node at specific position, with choosing to bypass the up/lower collection size limits.
     * @param index The position of key to remove.
     * @param bypass Whether or not to ignore the up/lower bound collection size limits.
     * @return If the deletion is success.
     */
    final boolean removeKeyAt(int index, boolean bypass) {
        if (bypass) {
            removeKeyAtBypass(index);
            return true;
        } else {
            return removeKeyAt(index);
        }
    }

    /**
     * Remove a key from the node at specific position, with ignoring the up/lower collection size limits.
     * @param index The position of key to remove.
     */
    final void removeKeyAtBypass(int index) {
        keys.remove(index);
    }

    /**
     * Remove a set of keys from the node.
     * @param keys The keys to be removed.
     * @return If the deletion is success.
     */
    final boolean removeKeys(Collection<Integer> keys) {
        int[] arr = keys.stream().mapToInt(i -> i).toArray();

        boolean ok;
        for (int key : arr) {
            ok = removeKey(key);
            if (!ok) return false;
        }
        return true;
    }

    /**
     * Remove a sub-item from the node.
     * @param s The sub-item to be removed.
     * @return If the deletion is success.
     */
    final boolean removeSubItemValue(S s) {
        boolean ok = !isSubItemsHungry() && subItems.contains(s);
        if (ok) {
            subItems.remove(s);
        }
        return ok;
    }

    /**
     * Remove a sub-item from the node at specific position.
     * @param index The position of sub-item to be removed.
     * @return If the deletion is success.
     */
    final boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry() && subItems.size() > index;
        if (ok) {
            removeSubItemValueAtBypass(index);
        }
        return ok;
    }

    /**
     * Remove a sub-item from the node at specific position, with choosing to bypass the up/lower collection size limits.
     * @param index The position of sub-item to remove.
     * @param bypass Whether or not to ignore the up/lower bound collection size limits.
     * @return If the deletion is success.
     */
    final boolean removeSubItemValueAt(int index, boolean bypass) {
        if (bypass) {
            removeSubItemValueAtBypass(index);
            return true;
        } else {
            return removeSubItemValueAt(index);
        }
    }

    /**
     * Remove a sub-item from the node at specific position, with ignoring to bypass the up/lower collection size limits.
     * @param index The position of sub-item to remove.
     * @return If the deletion is success.
     */
    final void removeSubItemValueAtBypass(int index) {
        subItems.remove(index);
    }

    /**
     * Remove a collection of sub-items from the node.
     * @param ss The sub-items to remove.
     * @return If the deletion is success.
     */
    final boolean removeSubItemsValues(Collection<S> ss) {
        Object[] arr = ss.stream().toArray();

        boolean ok;
        for (Object o : arr) {
            //noinspection unchecked
            ok = removeSubItemValue((S) o);
            if (!ok) return false;
        }
        return true;
    }

    /**
     * Gets the counts of sub-items.
     * @return How many sub-items are in this node.
     */
    final int subItemsSize() {
        return this.subItems.size();
    }

    /**
     * Gets the degree, which is useful for specifying the up/lower bound of keys and sub-items to store on the node.
     * @return The degree.
     */
    final int t() {
        return this.tree.getProperties().getDegree();
    }

    /**
     * Gets the total number of keys that can be stored on this node.
     * @return Total number of keys that can be stored on this node.
     */
    final int totalSpace() {
        return 2 * t() - 1;
    }

    /**
     * Gets the counts of keys.
     * @return How many keys are in this node.
     */
    final int usedSpace() {
        return n();
    }
}
