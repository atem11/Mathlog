package proofer.Lems;

import operations.Conjunction;
import operations.Expression;
import operations.Implication;
import operations.Variable;
import parser.Lexer;
import parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conj_TT {

    private static String[] proof = {
            "(A)",
            "(B)",
            "(A)->((B)->((A)&(B)))",
            "(B)->((A)&(B))",
            "(A)&(B)"
    };

    private static List<Expression> replProof = new ArrayList<>();

    static {
        Parser p = new Parser();
        Lexer l = new Lexer();
        for (String s : proof) {
            replProof.add(p.parse(l.lexer(s)));
        }
    }

    public static List<Expression> proof(Expression A, Expression B) {
        Map<Variable, Expression> vars = new HashMap<>();
        vars.put(new Variable("A"), A);
        vars.put(new Variable("B"), B);
        return Replacer.replace(replProof, vars);
    }
}
