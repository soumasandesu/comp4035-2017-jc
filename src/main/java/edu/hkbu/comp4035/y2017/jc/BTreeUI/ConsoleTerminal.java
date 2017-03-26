package edu.hkbu.comp4035.y2017.jc.BTreeUI;

import edu.hkbu.comp4035.y2017.jc.BTree;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ConsoleTerminal {

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
        parser.addArgument("fname")
                .help("the name of the data file (JSON) storing the search key values");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        System.out.println("Building an initial B+-Tree...");

        String importFilePath = ns.getString("fname");
        BTree<Void> bTree;
        try {
            bTree = new BTree<>(importFilePath);
        } catch (FileNotFoundException ignored) {
            System.out.printf("The file \"%s\" does not exist! \r\nExit.", importFilePath);
            System.exit(1);
        } catch (IOException e) {
            System.out.printf("Cannot read BTree file! Due to the following reason:\r\n%s\r\n", e.getMessage());
            e.printStackTrace();
            System.out.println("Exit.");
            System.exit(2);
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for your commands:");

            while (true) {
                System.out.print("> ");
                String cmd = scanner.nextLine();
                StringTokenizer st = new StringTokenizer(cmd);

                switch (st.nextToken()) {
                    case "insert":
                        break;
                    case "delete":
                        break;
                    case "print":
                        break;
                    case "stats":
                        break;
                    case "quit":
                        System.out.println("Thanks! Bye-bye ｽﾞｲ₍₍(ง˘ω˘)ว⁾⁾ｽﾞｲ ");
                        System.exit(0);
                }
            }
        }
    }
}
