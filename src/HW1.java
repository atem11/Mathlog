import axioms.AxiomChecker;
import javafx.util.Pair;
import operations.Expression;
import operations.Implication;
import parser.Lexer;
import parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HW1 {
    private static Map<Expression, Integer> hypotises = new HashMap<>();
    private static Map<Expression, Integer> truth = new HashMap<>();
    private static Map<Expression, List<Pair<Integer, Expression>>> deductNotTrue = new HashMap<>();
    private static Map<Expression, Pair<Integer, Expression>> deductTrue = new HashMap<>();

    private static String myTrim(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    private static void addMbTruth(Expression exp, int ind) {
        if (exp instanceof Implication) {
            Implication impl = (Implication) exp;
            Expression a = impl.getLeft();
            Expression b = impl.getRight();
            if (truth.containsKey(a)) {
                deductNotTrue.put(b, null);
                deductTrue.put(b, new Pair<>(ind, a));
            } else {
                deductNotTrue.computeIfAbsent(b, k -> new ArrayList<>());
                deductNotTrue.get(b).add(new Pair<>(ind, a));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        //Path pathToInputFile = Paths.get("input.txt");
        Path pathToInputFile = Paths.get("output.txt");
        //Path pathToInputFile = Paths.get("/home/artem/Projects/Mathlog/17.in");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        String firstLine = reader.readLine().replace("|-", ",");
        String[] hypot = firstLine.split(",");
        int indHyp = 1;
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        for (int i = 0; i < hypot.length - 1; ++i) {
            if (myTrim(hypot[i]).isEmpty()) {
                continue;
            }
            Expression exp = parser.parse(lexer.lexer(hypot[i]));
            hypotises.put(exp, indHyp++);
        }

        int indLine = 1;
        //try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
        try (PrintWriter out = new PrintWriter(new File("outputTmp.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = myTrim(line);
                if (line.isEmpty()) {
                    continue;
                }
                StringBuilder res = new StringBuilder();
                Expression root = parser.parse(lexer.lexer(line));
                res.append("(").append(indLine).append(") ").append(root.toString()).append(" ");

                Integer hypNum;
                Integer axiomNum;
                addMbTruth(root, indLine);
                if ((hypNum = hypotises.get(root)) != null) {
                    res.append("(Предп. ").append(hypNum).append(")");
                } else if ((axiomNum = AxiomChecker.checkAll(root)) != 0) {
                    res.append("(Сх. акс. ").append(axiomNum).append(")");
                } else {
                    Integer first = null, second = null;
                    if (deductTrue.containsKey(root)) {
                        Pair<Integer, Expression> prof = deductTrue.get(root);
                        first = truth.get(prof.getValue());
                        second = prof.getKey();
                    } else {
                        List<Pair<Integer, Expression>> listMbTruth = deductNotTrue.get(root);
                        if (listMbTruth != null) {
                            for (Pair<Integer, Expression> pair: listMbTruth) {
                                Expression a = pair.getValue();
                                Integer MP = pair.getKey();
                                Integer profN = truth.get(a);
                                if (profN != null) {
                                    deductNotTrue.put(root, null);
                                    deductTrue.put(root, pair);
                                    first = profN;
                                    second = MP;
                                    break;
                                }
                            }
                        }
                    }

                    if (first == null || second == null) {
                        res.append("(Не доказано)");
                        System.err.println("LUUUUUUL");
                    } else {
                        res.append("(M.P. ").append(second).append(", ").append(first).append(")");
                    }
                }

                truth.put(root, indLine);
                out.println(res);
                indLine++;
            }
        }
        reader.close();
        long endTime = System.currentTimeMillis();
        //System.out.println(endTime - startTime);
    }
}
