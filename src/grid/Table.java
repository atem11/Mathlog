package grid;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<List<Integer>> table;

    public Table(int size) {
        table = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            List<Integer> tmp = new ArrayList<>();
            for (int _i = 0; _i < size; ++_i) {
                tmp.add(1337);
            }
            table.add(tmp);
        }
    }

    public int get(int u, int v) {
        return table.get(u).get(v);
    }

    public void set(int u, int v, int val) {
        table.get(u).set(v, val);
    }

    public void set_2(int u, int v, int val) {
        table.get(u).set(v, val);
        table.get(v).set(u, val);
    }
}
