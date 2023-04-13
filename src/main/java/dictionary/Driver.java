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
            //((CompactPrefixTree) dict).printT();
            System.out.println(dict.toString());
            System.out.println(dict.checkPrefix("ca"));
            System.out.println(dict.checkPrefix("ch"));
            System.out.println("Checking word: " + dict.check("cat"));
            System.out.println("Checking word: " + dict.check("wring"));

//            CompactPrefixTree smth = new CompactPrefixTree();
//            smth.add("something");
//            smth.add("somethoughts");
//            smth.add("somewhere");
//            smth.add("somehow");
//            smth.add("somebeing");
//            smth.add("search");
//            smth.add("bear");
//            smth.add("because");
//            String[] arr = new String[100];
//            arr = smth.suggest("some", 50);
//            for (int i = 0; i < arr.length; i++) {
//                    System.out.println(arr[i]);
//            }

            // Add other "tests"
    }
}
