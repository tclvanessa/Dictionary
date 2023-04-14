package dictionary;

import java.util.Arrays;

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
            //((CompactPrefixTree) dict).printT();
            System.out.println(dict.toString());
            System.out.println(dict.checkPrefix("ca"));
            System.out.println(dict.checkPrefix("ch"));
            System.out.println("Checking word: " + dict.check("cat"));
            System.out.println("Checking word: " + dict.check("wring"));

            CompactPrefixTree smth = new CompactPrefixTree();
            smth.add("acedia");
            smth.add("aceldma");
            smth.add("acequia");
            smth.add("somebeing");
            smth.add("search");
            smth.add("bear");
            smth.add("because");
            System.out.println(smth.toString());
            String[] arr = new String[100];
//            arr = smth.suggest("ace", 4);
//            System.out.println(Arrays.toString(arr));

            // Add other "tests"
    }
}
