package dictionary;

/** The Driver class for CompactPrefixTree */
public class Driver {
    public static void main(String[] args) {
            Dictionary dict = new CompactPrefixTree();
            dict.add("cat");
            dict.add("cart");
            dict.add("carts");
            dict.add("case");
            dict.add("doge");
            dict.add("doghouse");
            dict.add("wrist");
            dict.add("wrath");
            dict.add("wristle");
            //((CompactPrefixTree) dict).print();
            ((CompactPrefixTree) dict).printT();
            System.out.println(dict.toString());
            System.out.println(dict.checkPrefix("ca"));
            System.out.println(dict.checkPrefix("ch"));
            System.out.println("Checking word: " + dict.check("cat"));
            System.out.println("Checking word: " + dict.check("wring"));

            // Add other "tests"
    }
}
