package grid;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public List<Integer> input;
    public List<Integer> output;

    public Node() {
        input = new ArrayList<>();
        output = new ArrayList<>();
    }

    public void add_in(int to) {
        input.add(to);
    }

    public void add_out(int to) {
        output.add(to);
    }


}
