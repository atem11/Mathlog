package grid;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.*;

public class Graph {
    private List<Node> tree;
    private int size;
    private Table eq;
    private Table plus;
    private Table mull;
    private Table next;

    public Graph(int sz) {
        tree = new ArrayList<>();
        for (int i = 0; i < sz; ++i) {
            tree.add(new Node());
        }
        size = sz;
        eq = new Table(sz);
        plus = new Table(sz);
        mull = new Table(sz);
        next = new Table(sz);
    }

    public void add_edge(int v, int to) {
        tree.get(v).add_out(to);
        tree.get(to).add_in(v);
    }

    public List<Integer> get_out(int ind) {
        return tree.get(ind).output;
    }

    public List<Integer> get_in(int ind) {
        return tree.get(ind).input;
    }

    public int size() {
        return size;
    }

    public Table get_equals() {
        for (int i = 0; i < size; ++i) {
            Deque<Integer> q = new ArrayDeque<>();
            List<Boolean> used = new ArrayList<>();
            for (int j = 0; j < size; ++j) {
                used.add(false);
            }

            for (int u : tree.get(i).output) {
                q.addLast(u);
                used.set(u, true);
            }
            while (!q.isEmpty()) {
                int to = q.pollFirst();
                eq.set(to, i, -1);
                eq.set(i, to, 1);
                for (int u : tree.get(to).output) {
                    if (!used.get(u)) {
                        q.addLast(u);
                        used.set(u, true);
                    }
                }
            }
            eq.set(i, i, 0);
        }
        return eq;
    }

    public Table get_plus() {
        for (int i = 0; i < size; ++i) {
            plus.set(i, i, i);
            for (int j = i + 1; j < size; ++j) {
                int min = -1;
                List<Integer> fail = new ArrayList<>();
                for (int tmp = 0; tmp < size; ++tmp) {
                    if (tmp == i || eq.get(tmp, i) == 1) {
                        if (tmp == j || eq.get(tmp, j) == 1) {
                            if (min == -1) {
                                min = tmp;
                            } else if (eq.get(tmp, min) == -1) {
                                min = tmp;
                            } else if (eq.get(tmp, min) == 1337) {
                                fail.add(tmp);
                            }
                        }
                    }
                }
                if (min != -1) {
                    plus.set_2(i, j, min);
                    for (int _i : fail) {
                        if (eq.get(_i, min) == 1337) {
                            plus.set_2(i, j, 1337);
                            break;
                        }
                    }
                } else {
                    plus.set_2(i, j, 1337);
                }
            }
        }

        return plus;
    }

    public Table get_mull() {
        for (int i = 0; i < size; ++i) {
            mull.set(i, i, i);
            for (int j = i + 1; j < size; ++j) {
                int max = -1;
                List<Integer> fail = new ArrayList<>();
                for (int tmp = 0; tmp < size; ++tmp) {
                    if (tmp == i || eq.get(tmp, i) == -1) {
                        if (tmp == j || eq.get(tmp, j) == -1) {
                            if (max == -1) {
                                max = tmp;
                            } else if (eq.get(tmp, max) == 1) {
                                max = tmp;
                            } else if (eq.get(tmp, max) == 1337) {
                                fail.add(tmp);
                            }
                        }
                    }
                }
                if (max != -1) {
                    mull.set_2(i, j, max);
                    for (int _i : fail) {
                        if (eq.get(_i, max) == 1337) {
                            mull.set_2(i, j, 1337);
                            break;
                        }
                    }
                } else {
                    mull.set_2(i, j, 1337);
                }
            }
        }

        return mull;
    }

}
