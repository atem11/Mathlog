import axioms.AxiomChecker;
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
import java.util.*;

public class HW2 {
    private static Set<Expression> hypotises = new HashSet<>();
    private static Set<Expression> truth = new HashSet<>();
    private static Map<Expression, List<Expression>> deductNotTrue = new HashMap<>();
    private static Map<Expression, Expression> deductTrue = new HashMap<>();
    private static Expression alpha;

    private static String myTrim(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    private static void addMbTruth(Expression exp) {
        if (exp instanceof Implication) {
            Implication impl = (Implication) exp;
            Expression a = impl.getLeft();
            Expression b = impl.getRight();
            if (truth.contains(a)) {
                deductNotTrue.put(b, null);
                deductTrue.put(b, a);
            } else {
                deductNotTrue.computeIfAbsent(b, k -> new ArrayList<>());
                deductNotTrue.get(b).add(a);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        String firstLine = reader.readLine().replace("|-", ",");
        String[] hypot = firstLine.split(",");
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        for (int i = 0; i < hypot.length - 2; ++i) {
            if (myTrim(hypot[i]).isEmpty()) {
                continue;
            }
            Expression exp = parser.parse(lexer.lexer(hypot[i]));
            hypotises.add(exp);
        }
        alpha = parser.parse(lexer.lexer(hypot[hypot.length - 2]));
        Expression H = parser.parse(lexer.lexer(hypot[hypot.length - 1]));
        H = new Implication(alpha, H);
        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            for (int i = 0; i < hypot.length - 2; ++i) {
                if (i != 0) {
                    out.print(",");
                }
                out.print(myTrim(hypot[i]));
            }
            out.print("|-");
            out.println(H.toString());


            String line;
            while ((line = reader.readLine()) != null) {
                line = myTrim(line);
                if (line.isEmpty()) {
                    continue;
                }
                Expression exp = parser.parse(lexer.lexer(line));
                addMbTruth(exp);
                if (hypotises.contains(exp) || AxiomChecker.checkAll(exp) != 0) {
                    out.println(exp);
                    Expression lst = new Implication(alpha, exp);
                    Expression mid = new Implication(exp, lst);
                    out.println(mid.toString());
                    out.println(lst.toString());
                } else if (exp.equals(alpha)) {
                    Expression e5 = new Implication(exp, exp);
                    Expression e1 = new Implication(exp, e5);
                    Expression e4 = new Implication(
                            exp,
                            new Implication(e5, exp)
                    );
                    Expression e3 = new Implication(e4, e5);
                    Expression e2 = new Implication(e1, e3);
                    out.println(e1.toString());
                    out.println(e2.toString());
                    out.println(e3.toString());
                    out.println(e4.toString());
                    out.println(e5.toString());
                } else {
                    Expression proof = null;
                    if(deductTrue.containsKey(exp)) {
                        proof = deductTrue.get(exp);
                    } else {
                        List<Expression> mbProof = deductNotTrue.get(exp);
                        for (Expression mB : mbProof) {
                            if (truth.contains(mB)) {
                                deductTrue.put(exp, mB);
                                deductNotTrue.put(exp, null);
                                proof = mB;
                                break;
                            }
                        }
                    }
                    Expression l3 = new Implication(alpha, exp);
                    Expression l2 = new Implication(
                            new Implication(alpha,
                                    new Implication(proof, exp)),
                            l3
                    );
                    Expression l1 = new Implication(
                            new Implication(alpha, proof),
                            l2
                    );
                    out.println(l1);
                    out.println(l2);
                    out.println(l3);
                }
                truth.add(exp);
            }
            reader.close();
        }
    }
}
