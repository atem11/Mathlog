import operations.Expression;
import operations.Implication;
import operations.Variable;
import parser.Lexer;
import parser.Parser;
import proofer.Deductor;
import proofer.Proof;
import substitutioner.Finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HW3 {
    private static List<Expression> hypotises = new ArrayList<>();
    private static List<Expression> variables = new ArrayList<>();
    private static Expression expression;

    private static String myTrim(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    private static List<Expression> genTable() {
        return null;
    }

    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        String firstLine = reader.readLine().replace("|=", ",");
        String[] hypot = firstLine.split(",");
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        for (int i = 0; i < hypot.length - 1; ++i) {
            if (myTrim(hypot[i]).isEmpty()) {
                continue;
            }
            Expression exp = parser.parse(lexer.lexer(hypot[i]));
            hypotises.add(exp);
        }
        Expression H = parser.parse(lexer.lexer(hypot[hypot.length - 1]));
        expression = H;
        for (int i = hypotises.size() - 1; i >= 0; --i) {
            expression = new Implication(hypotises.get(i), expression);
        }
        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            Finder finder = new Finder(expression);
            Map<Variable, Boolean> res = finder.find();
            variables = finder.retVars();
            if (res != null) {
                StringBuilder ans = new StringBuilder();
                ans.append("Высказывание ложно при");
                res.forEach((x, y) -> {
                    ans.append(" ").append(x).append("=");
                    if (y) {
                        ans.append("И,");
                    } else {
                        ans.append("Л,");
                    }
                });
                ans.deleteCharAt(ans.length() - 1);
                out.print(ans);
            } else {
                StringBuilder frstLine = new StringBuilder();
                for (Expression h : hypotises) {
                    frstLine.append(h).append(",");
                }
                if (frstLine.length() > 0) {
                    frstLine.deleteCharAt(frstLine.length() -1);
                }
                frstLine.append("|-");
                frstLine.append(H);
                out.println(frstLine);

                Proof proof = new Proof(variables, expression);
                List<Expression> ans = proof.genProof();
                for (int i = 0; i < hypotises.size(); ++i) {
                    ans = Deductor.makeProofToLeft(ans);
                }

                /*/////TEST////
                Map<Variable, Boolean> tmp = new HashMap<>();
                List<Expression> tmp1 = new ArrayList<>();
                //tmp1.add(new Negation(new Variable("A")));
                tmp.put(new Variable("A"), false);
                List<Expression> ans = Deductor.makeProofToRight(new Negation(new Variable("A")), proof.gen(tmp, expression), tmp1);
                //List<Expression> ans = proof.gen(tmp, expression);
                ////////////*/
                for (Expression l : ans) {
                    out.println(l);
                }
            }
            reader.close();
        }
    }
}
