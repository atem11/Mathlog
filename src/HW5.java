import kripke.Tree;
import kripke.World;
import operations.Expression;
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

public class HW5 {
    private static Expression expression;

    private static Set<Integer> findSet(World w) {
        Set<Integer> set = new HashSet<>();
        set.add(w.num);
        for (World world : w.getSons()) {
            set.addAll(findSet(world));
        }
        return set;
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

    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        expression = parser.parse(lexer.lexer(reader.readLine()));
        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            Tree tree = new Tree(expression);
            World ans = tree.genAll();
            if (ans != null) {
                //Преобразовать в алгебру Гейтинга

                Set<Set<Integer>> G = new HashSet<>();
                G.add(new HashSet<>());

                Deque<World> q = new ArrayDeque<>();
                q.addLast(ans);
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
                        if (checkSet(index.get(i), exp, ans)) {
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


                /*Gleb edition
                Set<Set<Integer>> G = new HashSet<>();
                Set<Integer> AllNum = new HashSet<>();
                G.add(new HashSet<>());
                for (Expression v : tree.retVar()) {
                    Set<Integer> tmp = new HashSet<>();
                    Deque<World> q = new ArrayDeque<>();
                    q.addLast(ans);
                    while (!q.isEmpty()) {
                        World world = q.pollFirst();
                        AllNum.add(world.num);
                        if (world.check(v)) {
                            tmp.add(world.num);
                        }
                        for (World w1 : world.getSons()) {
                            q.addLast(w1);
                        }

                    }
                    G.add(tmp);
                }
                System.out.println(G);

                while (true) {
                    Set<Set<Integer>> tmp = new HashSet<>();
                    for(Set<Integer> A : G) {
                        for (Set<Integer> B : G) {
                            Set<Integer> conj = new HashSet<>(A);
                            conj.removeAll(B);
                            Set<Integer> disj = new HashSet<>(A);
                            disj.addAll(B);
                            Set<Integer> impl = new HashSet<>(AllNum);
                            impl.removeAll(A);
                            impl.addAll(B);

                            tmp.add(conj);
                            tmp.add(disj);
                            tmp.add(impl);
                        }
                    }
                    System.out.println(tmp);
                    if (tmp != G) {
                        G = tmp;
                    } else {
                        break;
                    }
                }
                System.out.println(G);*/

            } else {
                out.println("Формула общезначима");
            }
        } finally {
            reader.close();
        }
    }
}
