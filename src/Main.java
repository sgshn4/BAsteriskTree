public class Main {
    public static void main(String[] args) {
        BAsteriskTree b = new BAsteriskTree(4);
        b.insert(8);
        b.insert(9);
        b.insert(10);
        b.insert(11);
        b.insert(15);
        b.insert(20);
        b.insert(17);
        b.insert(21);
        b.insert(35);
        b.insert(1);
        b.remove(35);

        b.show();

        if (b.contain(12)) {
            System.out.println("\nнайдено");
        } else {
            System.out.println("\nне найдено");
        }
    }
}