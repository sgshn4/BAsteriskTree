public class BAsteriskTree {

    private int t;
    private Node root;

    class Node {
        int n;
        int key[] = new int[2 * t - 1];
        Node child[] = new Node[2 * t];
        boolean leaf = true;

        public int find(int k) {
            for (int i = 0; i < this.n; i++) {
                if (this.key[i] == k) {
                    return i;
                }
            }
            return -1;
        }
    }

    public BAsteriskTree(int t) {
        this.t = t;
        this.root = new Node();
        root.n = 0;
        root.leaf = true;
    }

    private Node search(Node x, int key) {
        int i = 0;
        if (x == null) return x;
        for (i = 0; i < x.n; i++) {
            if (key < x.key[i]) break;
            if (key == x.key[i]) return x;
        }
        if (x.leaf) {
            return null;
        } else {
            return search(x.child[i], key);
        }
    }

    private void split(Node x, int pos, Node y) {
        Node z = new Node();
        z.leaf = y.leaf;
        z.n = t - 1;
        for (int i = 0; i < t - 1; i++) {
            z.key[i] = y.key[i + t];
        }
        if (!y.leaf) {
            for (int i = 0; i < t; i++) {
                z.child[i] = y.child[i + t];
            }
        }
        y.n = t - 1;
        for (int i = x.n - 1; i >= pos; i--) {
            x.key[i + 1] = x.key[i];
        }
        x.key[pos] = y.key[t - 1];
        x.n = x.n + 1;
    }

    public void insert(final int key) {
        Node r = root;
        if (r.n == 2 * t - 1) {
            Node s = new Node();
            root = s;
            s.leaf = false;
            s.n = 0;
            s.child[0] = r;
            split(s, 0, r);
            insertValue(s, key);
        } else {
            insertValue(r, key);
        }
    }
    private void insertValue(Node x, int k) {
        if (x.leaf) {
            int i = 0;
            for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {
                x.key[i + 1] = x.key[i];
            }
            x.key[i + 1] = k;
            x.n = x.n + 1;
        } else {
            int i = 0;
            for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {

            }
            i++;
            Node temp = x.child[i];
            if (temp.n == 2 * t - 1) {
                split(x, i, temp);
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

    private void show(Node x) {
        assert (x == null);
        for (int i = 0; i < x.n; i++) {
            System.out.println(x.key[i] + " ");
        }
        if (!x.leaf) {
            for (int i = 0; i < x.n + 1; i++) {
                show(x.child[i]);
            }
        }
    }

    public boolean contain(int k) {
        if (this.search(root, k) != null) {
            return true;
        } else {
            return false;
        }
    }
}
