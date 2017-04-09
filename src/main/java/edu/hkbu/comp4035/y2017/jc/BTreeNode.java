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
// TODO: javadocs for BTreeNode<S>
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = BTreeIndexNode.class, name = "BTreeIndexNode"),
//        @JsonSubTypes.Type(value = BTreeLeafNode.class, name = "BTreeLeafNode")
//})
abstract class BTreeNode<S> implements Serializable {
    //    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    final LinkedList<S> subItems;
    //    @JsonIgnore
    private final BTree tree;
    //    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private final LinkedList<Integer> keys;

    BTreeNode(BTree tree) {
        this.tree = tree;
        keys = new LinkedList<>();
        subItems = new LinkedList<>();
    }

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

    final boolean addKeyAt(int index, int key) {
        boolean ok = !isKeysFull();
        if (ok) {
            this.addKeyAtBypass(index, key);
        }
        return ok;
    }

    final boolean addKeyAt(int index, int key, boolean bypass) {
        if (bypass) {
            addKeyAtBypass(index, key);
            return true;
        } else {
            return addKeyAt(index, key);
        }
    }

    final void addKeyAtBypass(int index, int key) {
        this.keys.add(index, key);
    }

    final boolean addKeys(Collection<Integer> keys) {
        boolean ok;
        for (Integer key : keys) {
            ok = addKey(key);
            if (!ok) return false;
        }
        return true;
    }

    final boolean addSubItemValue(S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            ok = subItems.add(s);
        }
        return ok;
    }

    final boolean addSubItemValueAt(int index, S s) {
        boolean ok = !isSubItemsFull();
        if (ok) {
            addSubItemValueAtBypass(index, s);
        }
        return ok;
    }

    final boolean addSubItemValueAt(int index, S s, boolean bypass) {
        if (bypass) {
            addSubItemValueAtBypass(index, s);
            return true;
        } else {
            return addSubItemValueAt(index, s);
        }
    }

    final void addSubItemValueAtBypass(int index, S s) {
        subItems.add(index, s);
    }

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

    abstract BTreeNode<S> getEmptyClone();

    final int getKeyAt(int index) {
        return keys.get(index);
    }

    final Collection<Integer> getKeys() {
        //noinspection unchecked
        return (Collection<Integer>) keys.clone();
    }

    final Collection<Integer> getKeys(int start_inclusive, int end_exclusive) {
        return keys.subList(start_inclusive, end_exclusive);
    }

    final S getSubItemAt(int index) {
        return subItems.get(index);
    }

    final Collection<S> getSubItems() {
        //noinspection unchecked
        return (List<S>) subItems.clone();
    }

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

    public final BTree getTree() {
        return tree;
    }

    final boolean isKeysFull() {
        // max deg of keys = 2t - 1
        return n() >= 2 * t() - 1;
    }

    final boolean isKeysHungry() {
        // Every node other than the root must have at least t - 1 keys.
        // min deg of keys = t - 1
        return getTree().getRootNode() != this && n() < t() - 1;
    }

    public abstract boolean isLeaf();

    abstract boolean isSubItemsFull();

    abstract boolean isSubItemsHungry();

    @SuppressWarnings("ConstantConditions")
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

    final int n() {
        return this.keys.size();
    }

    private boolean removeKey(int key) {
        boolean ok = !isKeysHungry() && keys.contains(key);
        if (ok) {
            // can't get object reference, so... hack
            keys.remove(keys.indexOf(key));
        }
        return ok;
    }

    final boolean removeKeyAt(int index) {
        boolean ok = !isKeysHungry() && keys.size() > index;
        if (ok) {
            removeKeyAtBypass(index);
        }
        return ok;
    }

    final boolean removeKeyAt(int index, boolean bypass) {
        if (bypass) {
            removeKeyAtBypass(index);
            return true;
        } else {
            return removeKeyAt(index);
        }
    }

    final void removeKeyAtBypass(int index) {
        keys.remove(index);
    }

    final boolean removeKeys(Collection<Integer> keys) {
        int[] arr = keys.stream().mapToInt(i -> i).toArray();

        boolean ok;
        for (int key : arr) {
            ok = removeKey(key);
            if (!ok) return false;
        }
        return true;
    }

    final boolean removeSubItemValue(S s) {
        boolean ok = !isSubItemsHungry() && subItems.contains(s);
        if (ok) {
            subItems.remove(s);
        }
        return ok;
    }

    final boolean removeSubItemValueAt(int index) {
        boolean ok = !isSubItemsHungry() && subItems.size() > index;
        if (ok) {
            removeSubItemValueAtBypass(index);
        }
        return ok;
    }

    final boolean removeSubItemValueAt(int index, boolean bypass) {
        if (bypass) {
            removeSubItemValueAtBypass(index);
            return true;
        } else {
            return removeSubItemValueAt(index);
        }
    }

    final void removeSubItemValueAtBypass(int index) {
        subItems.remove(index);
    }

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

    final int t() {
        return this.tree.getProperties().getDegree();
    }

    final int keysSize() {
        return this.keys.size();
    }

    final int subItemsSize() {
        return this.subItems.size();
    }

    final int usedSpace() {
        return n();
    }

    final int totalSpace() {
        return 2 * t() - 1;
    }
}
