package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/** CompactPrefixTree class, implements Dictionary ADT and
 *  several additional methods. Can be used as a spell checker.
 *  Fill in code and feel free to add additional methods as needed.
 *  S23 */
public class CompactPrefixTree implements Dictionary {
    private Node root; // the root of the tree

    /** Default constructor  */
    public CompactPrefixTree() { root = new Node(); }

    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {
        root = new Node();
        // Read each word from the file, and call the add method
        try (FileReader f = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(f);
            String word = br.readLine();

            while (word != null) {
                add(word);
                word = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("No file found.");
        }
    }

    /** Adds a given word to the dictionary.
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method
    }

    /**
     * Checks if a given word is in the dictionary
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {
        return check(word.toLowerCase(), root); // Calling a private check method
    }

    /**
     * Checks if a given prefix is stored in the dictionary
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling a private checkPrefix method
    }

    /**
     * Returns a human-readable string representation of the compact prefix tree;
     * contains nodes listed using pre-order traversal and uses indentations to show the level of the node.
     * An asterisk after the node means the node's boolean flag is set to true.
     * The root is at the current indentation level (followed by * if the node's valid bit is set to true),
     * then there are children of the node at a higher indentation level.
     */
    public String toString() {
        String s = "";
        s = toString(root, 0); // Calling private toString method
        return s;
    }

    /**
     * Print out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {
        // Uses toString() method; outputs info to a file
        try (PrintWriter pw = new PrintWriter(filename)) {
            String str = toString(); // Stores toString result into the variable str to print
            pw.print(str);
        } catch (IOException e) {
            System.out.println("File error.");
        }
    }

    /**
     * Prints out the list of words.
     * I used this for testing to see how the list turned out.
     */
    public void print() {
        print("", root); // Calling private print method
    }

    /**
     * Prints out the nodes of the tree.
     * I used this for testing to see how the tree turned out.
     */
    public void printT() {
        printTrees("", root); // Calling private printTrees method
    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use ot the compact prefix tree.
     *
     * @param word The word to check
     * @param numSuggestions The length of the array to return. Note that if the word is
     * in the dictionary, this parameter will be ignored, and the array will contain a
     * single world.
     * @return An array of the closest entries in the dictionary to the target word
     */

    public String[] suggest(String word, int numSuggestions) {
        return suggestWords(word, root.prefix, this.root, numSuggestions); // Calling private suggestWords method
    }

    // ---------- Private helper methods ---------------

    /**
     *  A private add method that adds a given string to the tree
     * @param s the string to add
     * @param node the root of a tree where we want to add a new string

     * @return a reference to the root of the tree that contains s
     */
    private Node add(String s, Node node) {
        if (node == null)  { // base case: tree is null
            node = new Node();
            node.prefix = s;
            node.isWord = true;
            return node;
        }

        int indexDifference = findIndexDifference(s, node.prefix);
        if (indexDifference == node.prefix.length())  {
            if (s.length() > indexDifference) {
                int index = ((int) s.charAt(indexDifference)) - ((int) 'a');
                node.children[index] = add(s.substring(indexDifference), node.children[index]);
                return node;
            }
            node.isWord = true;
            return node;
        }

        String firstRemainder = node.prefix.substring(0, indexDifference);
        String secondRemainder = node.prefix.substring(indexDifference);

        Node newNode = new Node();
        newNode.prefix = firstRemainder;
        int index = ((int) node.prefix.charAt(indexDifference)) - ((int) 'a');
        newNode.children[index] = node;
        node.prefix = secondRemainder;

        if (indexDifference == s.length()) {
            newNode.isWord = true;
        }
        else {
            index = ((int) s.charAt(indexDifference)) - ((int) 'a');
            newNode.children[index] = add(s.substring(indexDifference), null);
        }
        return newNode ;
    }


    /** A private method to check whether a given string is stored in the tree.
     *
     * @param s the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String s, Node node) {
        // If the node is null, return false
        if (node == null) {
            return false;
        }

        // If the common prefix does not match the prefix, return false
        if (!commonPrefix(s, node.prefix).equals(node.prefix)) {
            return false;
        }

        // If word matches prefix, return the word
        if (node.prefix.equals(s)) {
            return node.isWord;
        }

        // If the common prefix does match the prefix,
        // recursively check if the suffix is in the subtree
        if (commonPrefix(s, node.prefix).equals(node.prefix)) {
            String suffix = suffix(s, node.prefix);
            int index = getIndex(suffix);
            return check(suffix, node.children[index]);
        }
        return false;
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node) {
        // Base case
        if (node == null) {
            return false;
        }

        // Return true if the prefix equals the common prefix
        // between the prefix and the prefix of the root
        if (prefix.equals(commonPrefix(prefix, node.prefix))) {
            return true;
        }

        // If the common prefix between the prefix and the prefix of the root equals
        // the prefix of the root, then use the suffix to recursively check in the subtree
        if (commonPrefix(prefix, node.prefix).equals(node.prefix)) {
            String suffix = suffix(prefix, node.prefix);
            int index = getIndex(suffix);
            return checkPrefix(suffix, node.children[index]);
        }
        return false;
    }

    /**
     * Private method to check if the word exists in the array.
     *
     * @param arr The array
     * @param prefix The prefix
     * @return True of the word is already in the array, otherwise returns false
     */
    private boolean checkWords(String[] arr, String prefix) {
        // If word is already in array, return true, otherwise false
        for (String word : arr) {
            if (prefix.equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A private recursive method to get the common prefix between two strings.
     *
     * @param wordOne First string
     * @param wordTwo Second string
     * @return The common prefix between the two strings
     */
    private String commonPrefix(String wordOne, String wordTwo) {
        String prefix = "";
        int count = 0;

        // Go until the character between the two strings differ,
        // or until the count is greater than the length of both the first and second string
        while (count < wordOne.length() && count < wordTwo.length()) {
            char first = wordOne.charAt(count);
            char second = wordTwo.charAt(count);
            if (first != second) {
                break;
            }
            count++;
        }
        prefix = wordOne.substring(0, count); // The common prefix

        return prefix;
    }

    /**
     * A private recursive method to get the suffix between two strings.
     *
     * @param wordOne
     * @param wordTwo
     * @return the suffix between the two strings
     */
    private String suffix(String wordOne, String wordTwo) {
        String suffix = "";
        int count = 0;

        // Go until the character between the two strings differ,
        // or until the count is greater than the length of both the first and second string
        while (count < wordOne.length() && count < wordTwo.length()) {
            char first = wordOne.charAt(count);
            char second = wordTwo.charAt(count);
            if (first != second) {
                break;
            }
            count++;
        }
        suffix = wordOne.substring(count, wordOne.length()); // The suffix of the string

        return suffix;
    }

    /**
     * Finds an index of the first character where strings s1 and s2 differ.
     * @param s1 the first string
     * @param s2 the second string
     * @return the index of the first character where the strings are different
     */
    private int findIndexDifference(String s1, String s2) {
        int index = 0;

        // While the index is less than both of the strings and the characters of the strings equal each other
        while (index < s1.length() && index < s2.length() &&  s1.charAt(index) == s2.charAt(index)) {
            index++;
        }
        return index;
    }

    /**
     * Finds the index of the string.
     * @param s String to get the index for
     * @return the index of the first character of the string
     */
    private int getIndex(String s) {
        char one = s.charAt(0);
        char two = 'a';

        int index = (int) one - (int) two; // Subtract ASCII numbers to find index
        return index;
    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use ot the compact prefix tree.
     *
     * @param s The word to check
     * @param prefix The prefix
     * @param node The node of the tree
     * @param numSuggestions The length of the array to return. Note that if the word is
     * in the dictionary, this parameter will be ignored, and the array will contain a
     * single world.
     * @return An array of the closest entries in the dictionary to the target word
     */
    private String[] suggestWords(String s, String prefix, Node node, int numSuggestions) {
        String word = prefix + s;

        // If word passed is in list, return array of length 1 that contains that word.
        if (check(word)) {
            String[] arrWords = new String[1];
            arrWords[0] = word;
            return arrWords;
        }

        // Array of numSuggestion different words
        String[] arrWords = new String[numSuggestions];

        // If the string matches the prefix
        if (s.equals(node.prefix)) {
            int count = 0;
            // Loop until count reaches numSuggestions
            while (count < numSuggestions) {
                // If the suffix exists, put the word into the array
                if (findSuffix(arrWords, prefix, node) != null) {
                    arrWords[count] = prefix + findSuffix(arrWords, prefix, node);
                }
                // Otherwise, run it through the method again with a smaller part of the word
                else {
                    return suggestWords(s.substring(0, s.length() - 2), "", root, numSuggestions);
                }
                count++;
            }
            return arrWords;
        }
        // If the string does not match the prefix
        else {
            String suffix = suffix(s, node.prefix);
            int index = getIndex(suffix);
            Node newNode = node.children[index];

            if (newNode == null) {
                int count = 0;

                while (count < numSuggestions) {
                    // If the suffix isn't null, add the word to the array
                    // Otherwise, run it through the method recursively
                    if (findSuffix(arrWords, prefix, node) != null) {
                        arrWords[count] = prefix + findSuffix(arrWords, prefix, node);
                    } else {
                        return suggestWords(s.substring(0, s.length() - 2), "", root, numSuggestions);
                    }
                    count++;
                }
                return arrWords;
            }
            String pre = prefix + node.prefix;
            return suggestWords(suffix, pre, newNode, numSuggestions);
        }
    }

    /**
     * Private method to find the different suffixes that matches with the given prefix.
     * Returns the prefix.
     *
     * @param arr The array
     * @param prefix The prefix
     * @param node The node of the tree
     * @return The prefix when the node is a word.
     */
    private String findSuffix(String[] arr, String prefix, Node node) {
        int count = 0;
        boolean found = checkWords(arr, prefix + node.prefix);

        // If node is a word, and it does not exist in the list, return prefix
        if (node.isWord && found == false) {
            return node.prefix;
        }

        prefix = prefix + node.prefix;

        // Loop until count reaches array of children length
        while (count < node.children.length) {
            Node newNode = node.children[count];

            // If the node in array of children isn't null, return prefix
            if (newNode != null && findSuffix(arr, prefix, newNode) != null) {
                return node.prefix + findSuffix(arr, prefix, newNode);
            }
            count++;
        }
        return null;
    }

    /**
     * Private method recursive helper method for toString that takes the node and the number of indentations,
     * and returns the tree (printed with indentations) in a string.
     *
     * @param node The node of the tree
     * @param numIndentations The number of indentations
     * @return The tree in a string.
     */
    private String toString(Node node, int numIndentations) {
        StringBuilder s = new StringBuilder();
        StringBuilder sb = new StringBuilder(numIndentations); // StringBuilder for indentation

        // Loop to create the indentations
        for (int i = 0; i < numIndentations; i++) {
            sb.append(" ");
        }
        String indent = sb.toString();

        // If the node is null, return new line
        if (node == null) {
            return "";
        }

        s.append(indent + node.prefix);

        // If the node is a word, append an asterisk and new line
        if (node.isWord) {
            s.append("*" + System.lineSeparator());
        }

        // If the node isn't a word, append the new line
        else {
            s.append(System.lineSeparator());
        }

        // Loop involving recursion with the node and another indent
        for (Node child : node.children) {
            s.append(toString(child, numIndentations + 1));
        }
        return s.toString();
    }

    /**
     * Private method for print().
     * Prints out the list of words.
     * @param s The word to print
     * @param node The node of the tree
     */
    private void print(String s, Node node) {
        // If node is a node, print it
        if (node.isWord) {
            System.out.println(s + node.prefix);
        }

        // Loop through the nodes in the array of children
        for (Node child : node.children) {
            if (child != null) {
                print(s + node.prefix, child);
            }
        }
    }

    /**
     * Private method for printT().
     * Prints out the nodes of the tree.
     * I used this for testing to see how the tree turned out.
     * @param s The word to print
     * @param node The node of the tree
     */
    private void printTrees(String s, Node node) {
        // Base case
        if (node == null) {
            return;
        }

        // If the node is a word, print the string with an asterisk
        if (node.isWord) {
            System.out.println(s + node.prefix + "*");
        }
        // If the node isn't a word, just print it
        if (!node.isWord) {
            System.out.println(s + node.prefix);
        }

        // Loop to move onto the next node in the array of children with an indentation
        for (Node child : node.children) {
            printTrees(s + " ", child);
        }
    }

    // --------- Private class Node ------------
    // Represents a node in a compact prefix tree
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node() {
            isWord = false;
            prefix = "";
            children = new Node[26]; // initialize the array of children
        }
    }
}
