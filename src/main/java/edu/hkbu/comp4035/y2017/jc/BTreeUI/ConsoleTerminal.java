package edu.hkbu.comp4035.y2017.jc.BTreeUI;

import edu.hkbu.comp4035.y2017.jc.BTree;
import edu.hkbu.comp4035.y2017.jc.BTreePrinter;
import edu.hkbu.comp4035.y2017.jc.BTreeProperties;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

class ConsoleTerminal {

    /*
    Below are the commands your program should support:

    insert <low> <high> <num>
    Insert num records randomly chosen in the range [low, high]

    delete <low> <high>
    Delete records with key values in the range [low, high]

    search <low> <high>
    Return the keys that fall in the range [low, high]

    print
    Print the whole B+ tree (format up to you, but be clear!)

    stats
    Show stats

    quit
    Terminate the program
     */

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("btree");
        MutuallyExclusiveGroup gp = parser.addMutuallyExclusiveGroup();
        gp.addArgument("-f", "--fname")
                .help("the name of the data file (JSON) storing the search key values");
        gp.addArgument("-d", "--degree")
                .help("degree of the B+ Tree. I.e.: the maximum keys in a node = 2t - 1, whereas minimum = t - 1");
        gp.required(true);

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        System.out.println("Building an initial B+-Tree...");

        BTree bTree; // cannot use type Void << http://stackoverflow.com/questions/5568409/
        String importFilePath = ns.getString("fname");
        if (importFilePath == null) {
            bTree = newBlankBTreeFromPrompt(ns.getString("degree"));
        } else {
            try {
                bTree = newBTreeFromJsonFilePath(importFilePath);
            } catch (Exception e) {
                System.out.println("Exit.");
                System.exit(1);
                return;
            }
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for your commands:");

            while (true) {
                System.out.print("> ");
                String ln = scanner.nextLine();
                StringTokenizer st = new StringTokenizer(ln);

                String cmd = "";
                if (st.hasMoreTokens()) {
                    cmd = st.nextToken();
                }

                int key1;
                int key2;

                // why not just parse commands by argparse :o) -- charles
                // and why not generalise -- switch-case is bad idea
                switch (cmd) {
                    case "insert": // 'insert key1:int value:*'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'insert key:int value:*'");
                            break;
                        }
                        try {
                            key1 = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException ignored) {
                            System.out.println("ERROR: key is not int");
                            break;
                        }
                        bTree.insert(key1, null);
                        break;
                    case "delete": // 'delete key1:int'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'delete key:int'");
                            break;
                        }
                        try {
                            key1 = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException ignored) {
                            System.out.println("ERROR: key is not int");
                            break;
                        }
                        bTree.delete(key1);
                        break;
                    case "search": // 'search keyLow:int [keyUp:int]'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'search keyLow:int [keyUp:int]'");
                            break;
                        }

                        try {
                            key1 = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException ignored) {
                            System.out.println("ERROR: keyLow is not int");
                            break;
                        }

                        if (st.countTokens() > 0) {
                            try {
                                key2 = Integer.parseInt(st.nextToken());
                            } catch (NumberFormatException ignored) {
                                System.out.println("ERROR: keyUp is not int");
                                break;
                            }
                        } else {
                            key2 = key1;
                        }

                        if (key2 > key1) {
                            int i = key1;
                            key1 = key2;
                            key2 = i;
                        }

                        bTree.search(key1, key2);
                        break;
                    case "print": // 'print [node keyContaining:int]'
//                        if (st.hasMoreTokens() && st.nextToken().equals("node")) {} // optional to do
                        System.out.println(BTreePrinter.doPrintAsString(bTree));
                        break;
                    case "stats": // 'stats'
//                        bTree.dumpStatistics().toString();
                        System.out.println("bTree.dumpStatistics().toString();"); // TODO: stats
                        break;
                    case "import": // 'import filepath:string(mime=application/json)'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'import filepath:string(mime=application/json)'");
                            break;
                        }
                        File file = new File(st.nextToken());
                        if (!file.exists()) {
                            System.out.println(String.format("File on path \"%s\" does not exist!", file.getPath()));
                            break;
                        } else if (!file.isFile()) {
                            System.out.println(String.format("File on path \"%s\" is not a file!", file.getPath()));
                            break;
                        } else if (!file.canRead()) {
                            System.out.println(String.format("File on path \"%s\" cannot be read!", file.getPath()));
                            break;
                        }
                        BTree newBTree;
                        try {
                            newBTree = BTree.importJsonFromFilePath(file.getPath());
                        } catch (Exception e) {
                            System.out.println(String.format("Read file error: %s", e.getMessage()));
                            e.printStackTrace();
                            break;
                        }
                        bTree = newBTree;
                        break;
                    case "export": // 'export filepath:string [overwrite:boolean=False]'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'export filepath:string [overwrite:boolean=False]'");
                            break;
                        }
                        String path = st.nextToken();
                        if (st.countTokens() > 1) {
                            boolean overwrite = false;
                            String strBool = st.nextToken();
                            String[] yes = {"Yes", "true", "1", "是", "係", "right", "overwrite"};
                            for (String s : yes) {
                                overwrite |= strBool.toLowerCase().equals(s);
                            }

                            if (!overwrite && new File(path).exists()) {
                                System.out.printf("Destination file %s already exists.\n", path);
                                break;
                            }
                        }

                        String json = bTree.exportJson();

                        try (BufferedWriter out = new BufferedWriter(new FileWriter(path))) {
                            out.write(json);
                        } catch (IOException e) {
                            System.out.printf("Cannot write to %s: %s\n", path, e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "reset": // 'reset deg:int'
                        if (st.countTokens() < 1) {
                            System.out.println("Syntax: 'reset deg:int'");
                            break;
                        }
                        newBTree = newBlankBTreeFromPrompt(st.nextToken());
                        if (newBTree != null) {
                            bTree = newBTree;
                        }
                        break;
                    case "about": // 'about'
                        System.out.println(about());
                        break;
                    case "quit": // 'quit'
                        System.out.println("Thanks! Bye-bye ｽﾞｲ₍₍(ง˘ω˘)ว⁾⁾ｽﾞｲ ");
                        System.exit(0);
                    default:
                        String msg = "Syntax: \n";
                        msg += "    insert key:int value:*\n";
                        msg += "    delete key:int\n";
                        msg += "    search keyLow:int [keyUp:int]\n";
                        msg += "    print [node keyContaining:int]\n";
                        msg += "    stats\n";
                        msg += "    import filepath:string(mime=application/json)\n";
                        msg += "    export filepath:string\n";
                        msg += "    about\n";
                        msg += "    quit\n";
                        System.out.println(msg);
                }
            }
        }
    }

    private static String about() {
        String msg = "";
        msg += "==========About==========\n";
        msg += "=========================\n";
        return msg;
    }

    private static BTree newBlankBTreeFromPrompt(String strDegree) {
        int deg;
        try {
            deg = Integer.parseInt(strDegree);
            if (deg < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ignored) {
            System.out.println("ERROR: The degree must be an positive integer");
            System.exit(-1);
            return null;
        }
        System.out.printf("Creating new empty B+ tree with degree=%d\n", deg);
        return BTree.createBTreeCharSequence(String.class, new BTreeProperties(deg));
    }

    private static BTree newBTreeFromJsonFilePath(String filePath) throws Exception { // yes i mud 9 dol throw
        try {
            return BTree.importJsonFromFilePath(filePath);
        } catch (FileNotFoundException e) {
            System.out.printf("The file \"%s\" does not exist!", filePath);
            System.exit(1);
            throw new Exception(e);
        } catch (Exception e) {
            System.out.printf("Cannot read BTree-JSON file:\r\n%s\r\n", e.getMessage());
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
