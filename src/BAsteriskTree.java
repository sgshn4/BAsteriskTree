public class BAsteriskTree {

    private int t;
    private Node root;
    private int minCountKeys;

    //Node
    public class Node {
        int childCount;
        int key[] = new int[2 * t - 1];
        Node child[] = new Node[2 * t];
        boolean isLeaf = true;

        public int find(int key) {
            for (int i = 0; i < this.childCount; i++) {
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
        root.childCount = 0;
        root.isLeaf = true;
        this.minCountKeys = ((t * 2 - 1) / 3) * 2;
    }



    // Поиск ключа
    private Node search(Node x, int key) {
        int i = 0;
        if (x == null)
            return x;
        for (i = 0; i < x.childCount; i++) {
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
        z.childCount = minCountKeys;
        for (int j = 0; j < minCountKeys; j++) {
            z.key[j] = y.key[j + minCountKeys + 1];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < minCountKeys + 1; j++) {
                z.child[j] = y.child[j + minCountKeys + 1];
            }
        }
        y.childCount = minCountKeys;
        for (int j = x.childCount; j >= pos + 1; j--) {
            x.child[j + 1] = x.child[j];
        }
        x.child[pos + 1] = z;

        for (int j = x.childCount - 1; j >= pos; j--) {
            x.key[j + 1] = x.key[j];
        }
        x.key[pos] = y.key[minCountKeys];
        x.childCount = x.childCount + 1;
    }

    // Вставка значения
    public void insert(int key) {
        Node r = root;
        if (r.childCount == 2 * t - 1) {
            Node s = new Node();
            root = s;
            s.isLeaf = false;
            s.childCount = 0;
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
            for (i = x.childCount - 1; i >= 0 && k < x.key[i]; i--) {
                x.key[i + 1] = x.key[i];
            }
            x.key[i + 1] = k;
            x.childCount = x.childCount + 1;
        } else {
            int i = 0;
            for (i = x.childCount - 1; i >= 0 && k < x.key[i]; i--) {
            }
            ;
            i++;
            Node tmp = x.child[i];
            if (tmp.childCount == 2 * t - 1) {
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
        for (int i = 0; i < x.childCount; i++) {
            System.out.print(x.key[i] + " ");
        }
        if (!x.isLeaf) {
            for (int i = 0; i < x.childCount + 1; i++) {
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

        b.show();

        if (b.contain(12)) {
            System.out.println("\nнайдено");
        } else {
            System.out.println("\nне найдено");
        }
        ;
    }
}