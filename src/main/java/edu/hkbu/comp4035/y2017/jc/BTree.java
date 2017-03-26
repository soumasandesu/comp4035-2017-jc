package edu.hkbu.comp4035.y2017.jc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// TODO: Javadoc for class BTree
public class BTree<VType> {
    // Note: you cannot confirm what key type will be before completion of invoking ctor.
    private BTreeNode<VType> rootNode;
    private BTreeProperties properties;

    /**
     * The constructor for the BTree takes in a filename, and checks if a file with that name already exists.
     * If the file exists, we "open" the file and build an initial B+-tree based on the key values in the file.
     * Otherwise, we return an error message and the program terminates.
     */
    public BTree(String pathname) {
        File file = new File(pathname);
        try {
            String content = StringUtils.join(Files.readAllLines(file.toPath()), "");
            importJson(content);
        } catch (IOException e) {
            System.out.printf("Cannot read BTree file! Due to the following reason:\r\n%s\r\n", e.getMessage());
            e.printStackTrace();
        }
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

    public BTree insert(int key, VType rid) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public BTree delete(int key) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public BTree search(int key, VType rid) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public BTreeProperties getProperties() {
        return properties;
    }
}
