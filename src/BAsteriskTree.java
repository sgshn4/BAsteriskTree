public class BAsteriskTree {

    private int t;
    private Node root;
    private int maxCountKeys;

    //Node
    public class Node {
        int keyCound;
        int key[] = new int[2 * t - 1];
        Node child[] = new Node[2 * t];
        boolean isLeaf = true;

        public int find(int key) {
            for (int i = 0; i < this.keyCound; i++) {
                if (this.key[i] == key) {
                    return i;
                }
            }
            return -1;
        };
    }

    public BAsteriskTree(int t) {
        this.t = t;
        root = new Node();
        root.keyCound = 0;
        root.isLeaf = true;
        this.maxCountKeys = (t / 3) * 2;
    }



    // Поиск ключа
    private Node search(Node x, int key) {
        int i = 0;
        if (x == null)
            return x;
        for (i = 0; i < x.keyCound; i++) {
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
        z.keyCound = maxCountKeys - 1;
        for (int j = 0; j < maxCountKeys - 1; j++) {
            z.key[j] = y.key[j + maxCountKeys];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < maxCountKeys; j++) {
                z.child[j] = y.child[j + maxCountKeys];
            }
        }
        y.keyCound = maxCountKeys - 1;
        for (int j = x.keyCound; j >= pos + 1; j--) {
            x.child[j + 1] = x.child[j];
        }
        x.child[pos + 1] = z;

        for (int j = x.keyCound - 1; j >= pos; j--) {
            x.key[j + 1] = x.key[j];
        }
        x.key[pos] = y.key[maxCountKeys - 1];
        x.keyCound = x.keyCound + 1;
    }

    // Вставка значения
    public void insert(int key) {
        Node r = root;
        if (r.keyCound == 2 * maxCountKeys - 1) {
            Node s = new Node();
            root = s;
            s.isLeaf = false;
            s.keyCound = 0;
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
            for (i = x.keyCound - 1; i >= 0 && k < x.key[i]; i--) {
                x.key[i + 1] = x.key[i];
            }
            x.key[i + 1] = k;
            x.keyCound = x.keyCound + 1;
        } else {
            int i = 0;
            for (i = x.keyCound - 1; i >= 0 && k < x.key[i]; i--) {
            }
            ;
            i++;
            Node tmp = x.child[i];
            if (tmp.keyCound >= 2 * maxCountKeys - 1) {
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
        for (int i = 0; i < x.keyCound; i++) {
            System.out.print(x.key[i] + " ");
        }
        if (!x.isLeaf) {
            for (int i = 0; i < x.keyCound + 1; i++) {
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
//        b.insert(21);

        b.show();

        if (b.contain(12)) {
            System.out.println("\nнайдено");
        } else {
            System.out.println("\nне найдено");
        }
        ;
    }
}