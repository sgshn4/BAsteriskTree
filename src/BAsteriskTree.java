public class BAsteriskTree {

    private int t;
    private Node root;
    private int minCountKeys;

    //Node
    public class Node {
        int keyCount;
        int key[] = new int[2 * t - 1];
        Node child[] = new Node[2 * t];
        boolean isLeaf = true;

    }

    public BAsteriskTree(int t) {
        this.t = t;
        root = new Node();
        root.keyCount = 0;
        root.isLeaf = true;
        this.minCountKeys = ((t * 2 - 1) / 3) * 2;
    }



    // Поиск ключа
    private Node search(Node x, int key) {
        int i = 0;
        if (x == null)
            return x;
        for (i = 0; i < x.keyCount; i++) {
            if (key < x.key[i]) {
                break;
            }
            if (key == x.key[i]) {
                return x;
            }
        }
        if (x.isLeaf) {
            return null;
        } else {
            return search(x.child[i], key);
        }
    }

    // Разбиение узла
    private void split(Node x, int pos, Node y) {
        Node z = new Node();
        z.isLeaf = y.isLeaf;
        for (int j = 1; j < t - 1; j++) {
            z.key[j - 1] = y.key[j + t];
            z.keyCount++;
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++) {
                z.child[j] = y.child[j + t];
            }
        }
        y.keyCount = minCountKeys;
        for (int i = y.key.length - 1; i > minCountKeys; i--) {
            y.key[i] = 0;
        }
        for (int j = x.keyCount; j >= pos + 1; j--) {
            x.child[j + 1] = x.child[j];
        }
        x.child[pos + 1] = z;

        for (int j = x.keyCount - 1; j >= pos; j--) {
            x.key[j + 1] = x.key[j];
        }
        x.key[pos] = y.key[minCountKeys];
        x.keyCount = x.keyCount + 1;
    }

    // Вставка значения
    public void insert(int key) {
        Node r = root;
        if (r.keyCount == 2 * t - 1) {
            Node s = new Node();
            root = s;
            s.isLeaf = false;
            s.keyCount = 0;
            s.child[0] = r;
            split(s, 0, r);
            insertValue(s, key);
        } else {
            insertValue(r, key);
        }
    }

    // Вставка узла
    private void insertValue(Node x, int k) {
        if (x.isLeaf) {
            int i = 0;
            for (i = x.keyCount - 1; i >= 0 && k < x.key[i]; i--) {
                x.key[i + 1] = x.key[i];
            }
            x.key[i + 1] = k;
            x.keyCount = x.keyCount + 1;
        } else {
            int i = 0;
            for (i = x.keyCount - 1; i >= 0 && k < x.key[i]; i--) {
            }
            ;
            i++;
            Node tmp = x.child[i];
            if (tmp.keyCount == 2 * t - 1) {
                split(x, i, tmp);
                if (k > x.key[i]) {
                    i++;
                }
            }
            insertValue(x.child[i], k);
        }
    }

    public void show() {
        show(root);
    }

    // Вывод на экран
    private void show(Node x) {
        assert (x == null);
        for (int i = 0; i < x.keyCount; i++) {
            System.out.print(x.key[i] + " ");
        }
        if (!x.isLeaf) {
            for (int i = 0; i < x.keyCount + 1; i++) {
                show(x.child[i]);
            }
        }
    }

    // Проверка, содержится ли ключ
    public boolean contain(int k) {
        if (this.search(root, k) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        BAsteriskTree b = new BAsteriskTree(3);
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

        b.show();

        if (b.contain(12)) {
            System.out.println("\nнайдено");
        } else {
            System.out.println("\nне найдено");
        }
        ;
    }
}