package edu.hkbu.comp4035.y2017.jc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Collection;

// TODO: Javadoc for class BTree
public class BTree<VType> {
    // Note: you cannot confirm what key type will be before completion of invoking ctor.
    private BTreeNode rootNode;
    private BTreeProperties properties;

    /**
     * The constructor for the BTree takes in a filename, and checks if a file with that name already exists.
     * If the file exists, we "open" the file and build an initial B+-tree based on the key values in the file.
     * Otherwise, we return an error message and the program terminates.
     */
    public BTree(String pathname) throws IOException {
        File file = new File(pathname);

        if (!file.isFile()) throw new FileNotFoundException("404: " + pathname);

        String content = StringUtils.join(Files.readAllLines(file.toPath()), "");
        importJson(content);
    }

    /**
     * The destructor of BTree just "closes" the index. THis includes de-allocating
     * the memory space for the index. Note that it does not delete the file.
     */
    // TODO: This method is not guaranteed to be invoked by the GC.
    // TODO: Use try-finally if possible
    // See: http://stackoverflow.com/questions/171952/is-there-a-destructor-for-java
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private BTree importJson(String content) {
        try {
            BTreeDumpJsonFormat.Parser.fromString(content);
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: throwing on importJson(String)
        }
        return null;
    }

    private String exportJson() {
        try {
            return BTreeDumpJsonFormat.Stringfy.asString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // TODO: throwing on exportJson()
        }
        return null;
    }

    public int getHeight() {
        throw new UnsupportedOperationException("not yet implemented.");
        // TODO: get tree height
    }

    BTreeNode getRootNode() {
        return rootNode;
    }

    void setRootNode(BTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * This method inserts a pair ({@code key}, {@code rid}) into the B+-Tree Index ({@code rid} can always be assumed
     * to be {@code 0} in your implementation). The actual pair ({@code key}, {@code rid}) is inserted into a leaf node.
     * But this insertion may cause one or more ({@code key}, {@code rid}) pair to be inserted into index nodes. You
     * should always check to see if the current node has enough space before you insert. If you don't have enough
     * space, you have to split the current node by creating a new node, and copy some of the data over from the current
     * node to the new node. Splitting will cause a new entry to be added in the parent node.<br/>
     * <br/>
     * Splitting of the root node should be considered separately, since if we have a new root, we need to update the
     * root pointer to reflect the changes. Splitting of a leaf node should also be considered separately since the leaf
     * nodes are linked as a link list.<br/>
     * <br/>
     * Due to the complexity of this function, we recommend that you write separate functions for different cases. For
     * example, it is a good idea to write a function to insert into a leaf node, and a function to insert into an index
     * node.
     * @param key Key for identifying the data to be stored on the tree indexing system.
     * @param rid The data.
     * @return The current operating {@code BTree} object for further chain operation.
     */
    public BTree insert(int key, VType rid) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * This method deletes an entry ({@code key}, {@code rid}) from a leaf node. Deletion from a leaf node may cause one
     * or more entries in the index node to be deleted. You should always check if a node underflows (less than 50% full)
     * after deletion. If a node becomes underflows, merging or redistribution will occur (read and implement the
     * algorithm in the notes).<br/>
     * <br/>
     * You should consider different scenarios separately (maybe write separate functions for them). You should consider
     * deletion from a leaf node and index node separately. Deletion from the root should also be considered separately.
     * @param key Key for identifying the data, which its reference to be removed from the tree indexing system.
     * @return The current operating {@code BTree} object for further chain operation.
     */
    public BTree delete(int key) {
        throw new UnsupportedOperationException("not yet implemented.");
    }


    /**
     * This method implements range queries. Given a search range ({@code key1}, {@code key2}), the method returns all
     * the qualifying key values in the range of between {@code key1} and {@code key2} in the B+-tree. If such keys are
     * not found, it returns nothing. <i><b>Be careful with the duplicate keys that span over multiple pages.</b></i>
     * @param key1 The first key which may identify a data reference.
     * @param key2 The second key which may identify another data reference.
     * @return The collection of {@code BTreeNode}s which are within range between {@code key1} and {@code key2}
     * <u>inclusively</u>.
     */
    public Collection<BTreeNode<VType>> search(int key1, int key2) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * In this method, you are required to collect statistics to reflect the performance of your B+- Tree implementation.
     * This method should print out the following statistics of your B+-Tree.<br/>
     * <ol>
     *     <li>Total number of nodes in the tree</li>
     *     <li>Total number of data entries in the tree</li>
     *     <li>Total number of index entries in the tree</li>
     *     <li>Average fill factor (used space/total space) of the nodes.</li>
     *     <li>Height of tree</li>
     * </ol>
     * These statistics should serve you in making sure that your code executes correctly. For example, the fill factor
     * of each node should be greater than {@code 0.5}. You should make sure that DumpStatistics performs this operation.
     * @return An <i>immutable</i> B+ Tree statistics object which records the status of the B+ Tree on the moment.
     * @see <a href="http://stackoverflow.com/questions/279507/what-is-meant-by-immutable">java - What is meant by immutable? - Stack Overflow</a>
     */
    public BTreeStatistics dumpStatistics() {
        // TODO: return new BTreeStatistics(numberOfNodes, numberOfDataEntries, numberOfIndexEntries, averageFillFactor, height);
        return null;
    }

    /**
     * These are helper functions that should help you debug, by showing the tree contents.
     * {@code PrintTree} must be implemented and {@code PrintNode} is optional.
     * @param ps The {@code PrintStream} to be used as the destination of text output, for example, "{@code System.out}".
     */
    public void printTree(PrintStream ps) {
        // TODO: ps.println(); // just like System.out.println();
    }

    /*
    // Optional
    public void printNode(PrintStream ps, int nodeKey) {

    }
    */

    public BTreeProperties getProperties() {
        return properties;
    }

    boolean isValueMatchActualType(Object value) {
        return getGenericTypeClass().isInstance(value);
    }

    private Class<VType> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            //noinspection unchecked
            return (Class<VType>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }
}
