import javafx.util.Pair;
import kripke.Tree;
import kripke.World;
import operations.Expression;
import operations.Variable;
import parser.Lexer;
import parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HW6 {

    private static Expression expression;

    private static Set<Integer> findSet(World w) {
        Set<Integer> set = new HashSet<>();
        set.add(w.num);
        for (World world : w.getSons()) {
            set.addAll(findSet(world));
        }
        return set;
    }

    private static boolean checkSet(Set<Integer> set, Expression v, World root) {
        boolean f = true;
        for (int i : set) {
            f = checkWorld(root, i, v);
            if (!f) {
                break;
            }
        }
        return f;
    }

    private static boolean checkWorld(World w, int num, Expression v) {
        if (w.num == num) {
            return w.check(v);
        } else {
            boolean f = false;
            for (World world : w.getSons()) {
                f = checkWorld(world, num, v);
                if (f) {
                    break;
                }
            }
            return f;
        }
    }

    private static boolean checkKripke(World w, Set<Expression> set) {
        boolean f = true;
        for (Expression e : set) {
            if (!w.check(e)) {
                 return false;
            }
        }
        Set<Expression> set1 = new HashSet<>(set);
        set1.addAll(w.real);
        for (World ww : w.getSons()) {
            if (!checkKripke(ww, set1)) {
                f = false;
            }
        }
        return f;
    }

    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        expression = parser.parse(lexer.lexer(reader.readLine()));
        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            List<World> worlds = new ArrayList<>();
            World root = new World();
            root.num = 0;
            worlds.add(root);
            String ln;
            while ((ln = reader.readLine()) != null) {
                int tab = 0;
                if (ln.length() > 0) {
                    World w = new World();
                    int ind = 0;
                    while (ln.charAt(ind) != '*') {
                        tab++;
                        ind++;
                    }
                    w.num = tab + 1;
                    ind++;
                    String last = ln.substring(ind);
                    last = last.replace(',', ' ');
                    String[] vars = last.split("\\s+");
                    for (String s : vars) {
                        if (s.length() > 0) {
                            w.addVar(new Variable(s));
                        }
                    }
                    World p = null;
                    for (World ww : worlds) {
                        if (ww.num == tab) {
                            p = ww;
                        }
                    }
                    worlds.add(w);
                    p.addSon(w);
                    w.parent = p;
                }
            }
            //System.out.println("KEK");
            if (!checkKripke(root, new HashSet<>())) {
                out.println("Не модель Крипке");
            } else {
                Tree tree = new Tree(root, expression);
                if (tree.check()) {
                    out.println("Не опровергает формулу");
                } else {
                    tree.doNum();
                    Set<Set<Integer>> G = new HashSet<>();
                    G.add(new HashSet<>());

                    Deque<World> q = new ArrayDeque<>();
                    q.addLast(root);
                    while (!q.isEmpty()) {
                        World world = q.pollFirst();
                        G.add(findSet(world));
                        for (World w1 : world.getSons()) {
                            q.addLast(w1);
                        }
                    }

                    while (true) {
                        Set<Set<Integer>> tmp = new HashSet<>();
                        for (Set<Integer> A : G) {
                            for (Set<Integer> B : G) {
                                Set<Integer> disj = new HashSet<>(A);
                                disj.addAll(B);
                                tmp.add(disj);
                            }
                        }
                        if (tmp.size() == G.size()) {
                            break;
                        } else {
                            G = new HashSet<>(tmp);
                        }
                    }

                    List<Set<Integer>> index = new ArrayList<>(G);
                    out.println(index.size());
                    for (int i = 0; i < index.size(); ++i) {
                        for (int j = 0; j < index.size(); ++j) {
                            Set<Integer> I = index.get(i);
                            Set<Integer> Ik = index.get(j);
                            if (Ik.containsAll(I)) {
                                out.print(j + 1);
                                out.print(" ");
                            }
                        }
                        out.println();
                    }
                    StringBuilder Ans = new StringBuilder();
                    for (Expression exp : tree.retVar()) {
                        Ans.append(exp + "=");
                        int ind_ans = -1, ind_size = -1;
                        for (int i = 0; i < index.size(); ++i) {
                            if (checkSet(index.get(i), exp, root)) {
                                if (ind_size < index.get(i).size()) {
                                    ind_size = index.get(i).size();
                                    ind_ans = i + 1;
                                }
                            }
                        }
                        Ans.append(ind_ans + ",");
                    }
                    Ans.deleteCharAt(Ans.length() - 1);
                    out.println(Ans);
                }
            }

        } finally {
            reader.close();
        }
    }
}
