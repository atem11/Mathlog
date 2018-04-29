package proofer.Lems;

import operations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Replacer {
    private static Map<Variable, Expression> repl = new HashMap<>();

    public static List<Expression> replace(List<Expression> proof, Map<Variable, Expression> repl) {
        Replacer.repl = repl;
        List<Expression> res = new ArrayList<>();
        for (Expression e : proof) {
            res.add(replaceOne(e));
        }
        return res;
    }

    private static Expression replaceOne(Expression line) {
        if (line instanceof Variable) {
            return repl.get(line);
        } else {
            if (line instanceof Negation) {
                return new Negation(replaceOne(((Negation) line).get()));
            } else if (line instanceof Implication) {
                return new Implication(replaceOne(((Implication) line).getLeft()), replaceOne(((Implication) line).getRight()));
            } else if (line instanceof Conjunction) {
                return new Conjunction(replaceOne(((Conjunction) line).getLeft()), replaceOne(((Conjunction) line).getRight()));
            } else if (line instanceof Disjunction) {
                return new Disjunction(replaceOne(((Disjunction) line).getLeft()), replaceOne(((Disjunction) line).getRight()));
            }
        }
        return null;
    }
}
