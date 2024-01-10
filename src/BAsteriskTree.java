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

        public int findKey(int k) {
            int idx = 0;
            while (idx < keyCount && key[idx] < k) {
                idx++;
            }
            return idx;
        }

        public void fill(int idx) {
            if (idx != 0 && child[idx - 1].keyCount >= t) {
                borrowFromPrev(idx);
            } else if (idx != keyCount && child[idx + 1].keyCount >= t) {
                borrowFromNext(idx);
            } else {
                if (idx != keyCount) {
                    merge(idx);
                } else {
                    merge(idx - 1);
                }
            }
        }

        // Метод для получения предшественника ключа по индексу
        public int getPred(int idx) {
            Node curr = child[idx];
            while (!curr.isLeaf) {
                curr = curr.child[curr.keyCount];
            }
            return curr.key[curr.keyCount - 1];
        }

        // Метод для получения преемника ключа по индексу
        public int getSucc(int idx) {
            Node curr = child[idx + 1];
            while (!curr.isLeaf) {
                curr = curr.child[0];
            }
            return curr.key[0];
        }

        public void remove(int k) {
            int idx = findKey(k);

            if (idx < keyCount && key[idx] == k) {
                if (isLeaf) {
                    removeFromLeaf(idx);
                } else {
                    removeFromNonLeaf(idx);
                }
            } else {
                if (isLeaf) {
                    System.out.println("Ключ " + k + " не найден в узле");
                    return;
                }

                boolean flag = (idx == keyCount);

                if (child[idx].keyCount < t) {
                    fill(idx);
                }

                if (flag && idx > keyCount) {
                    child[idx - 1].remove(k); // Рекурсивный вызов метода remove для левого дочернего узла
                } else {
                    child[idx].remove(k);
                }
            }
        }

        // Метод для удаления ключа из листового узла по индексу
        public void removeFromLeaf(int idx) {
            for (int i = idx + 1; i < keyCount; i++) {
                key[i - 1] = key[i];
            }
            keyCount--;
        }

        // Метод для удаления ключа из нелистового узла по индексу
        public void removeFromNonLeaf(int idx) {
            int k = key[idx];
            if (child[idx].keyCount >= t) {
                int pred = getPred(idx);
                key[idx] = pred;
                child[idx].remove(pred);
            } else if (child[idx + 1].keyCount >= t) {
                int succ = getSucc(idx);
                key[idx] = succ;
                child[idx + 1].remove(succ);
            } else {
                merge(idx);
                child[idx].remove(k);
            }
        }

        // Метод для заимствования ключа из предыдущего дочернего узла
        public void borrowFromPrev(int idx) {
            Node c = child[idx];
            Node sibling = child[idx - 1];

            for (int i = c.keyCount - 1; i >= 0; i--) {
                c.key[i + 1] = c.key[i];
            }

            if (!c.isLeaf) {
                for (int i = c.keyCount; i >= 0; i--) {
                    c.child[i + 1] = c.child[i];
                }
            }

            c.key[0] = key[idx - 1];

            if (!c.isLeaf) {
                c.child[0] = sibling.child[sibling.keyCount];
            }

            key[idx - 1] = sibling.key[sibling.keyCount - 1];

            c.keyCount++;
            sibling.keyCount--;
        }

        // Метод для заимствования ключа из следующего дочернего узла
        public void borrowFromNext(int idx) {
            Node c = child[idx];
            Node sibling = child[idx + 1];

            c.key[c.keyCount] = key[idx];

            if (!c.isLeaf) {
                c.child[c.keyCount + 1] = sibling.child[0];
            }

            key[idx] = sibling.key[0];

            for (int i = 1; i < sibling.keyCount; i++) {
                sibling.key[i - 1] = sibling.key[i];
            }

            if (!sibling.isLeaf) {
                for (int i = 1; i <= sibling.keyCount; i++) {
                    sibling.child[i - 1] = sibling.child[i];
                }
            }

            c.keyCount++;
            sibling.keyCount--;
        }

        // Метод для слияния дочернего узла с его следующим соседом
        public void merge(int idx) {
            Node c = child[idx];
            Node sibling = child[idx + 1];

            c.key[t - 1] = key[idx];

            for (int i = 0; i < sibling.keyCount; i++) {
                c.key[i + t] = sibling.key[i];
            }

            if (!c.isLeaf) {
                for (int i = 0; i <= sibling.keyCount; i++) {
                    c.child[i + t] = sibling.child[i];
                }
            }

            for (int i = idx + 1; i < keyCount; i++) {
                key[i - 1] = key[i];
            }

            for (int i = idx + 2; i <= keyCount; i++) {
                child[i - 1] = child[i];
            }

            c.keyCount += sibling.keyCount + 1;
            keyCount--;

            sibling = null;
        }
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

    public void remove(int k) {
        if (root == null) {
            System.out.println("The tree is empty");  // Если дерево пустое, выводим сообщение
            return;
        }
        root = remove(root, k);

        if (root.keyCount == 0) {  // Если корень больше не содержит ключей
            if (root.isLeaf) {
                root = null;  // Если корень листовой, делаем его null
            } else {
                root = root.child[0];  // Иначе корнем становится его единственный дочерний узел
            }
        }
    }

    private Node remove(Node x, int k) {
        int idx = x.findKey(k);  // Находим индекс ключа k в узле x

        if (idx < x.keyCount && x.key[idx] == k) {  // Если ключ найден в узле x
            if (x.isLeaf) {
                x.removeFromLeaf(idx);  // Удаляем ключ из листового узла
            } else {
                x.removeFromNonLeaf(idx);  // Удаляем ключ из нелистового узла
            }
        } else {
            if (x.isLeaf) {
                System.out.println("The key " + k + " does not exist in the tree");  // Если ключ не найден и узел листовой, выводим сообщение
                return x;
            }

            boolean flag = (idx == x.keyCount);  // Флаг для определения, находится ли индекс за пределами массива ключей

            if (x.child[idx].keyCount < t) {  // Если дочерний узел содержит менее t ключей
                x.fill(idx);  // Вызываем метод fill для заполнения дочернего узла
            }

            if (flag && idx > x.keyCount) {
                x.child[idx - 1] = remove(x.child[idx - 1], k);  // Рекурсивно вызываем метод удаления для соответствующего дочернего узла
            } else {
                x.child[idx] = remove(x.child[idx], k);  // Рекурсивно вызываем метод удаления для соответствующего дочернего узла
            }
        }
        return x;
    }
}