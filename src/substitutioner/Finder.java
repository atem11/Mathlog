package substitutioner;

import operations.*;

import java.util.*;

public class Finder {
    private Expression expression;
    private Map<Variable, Boolean> vals = new HashMap<>();
    private Set<Variable> vars = new HashSet<>();

    public Finder(Expression exp) {
        expression = exp;
    }

    private boolean implication(boolean a, boolean b) {
        return !a || b;
    }

    private boolean checkRec(Expression node) {
        if (node instanceof Variable) {
            return vals.get(node);
        } else {
            if (node instanceof Implication) {
                return implication(checkRec(((Implication) node).getLeft()), checkRec(((Implication) node).getRight()));
            } else if (node instanceof Conjunction) {
                return (checkRec(((Conjunction) node).getLeft()) && checkRec(((Conjunction) node).getRight()));
            } else if (node instanceof Disjunction) {
                return (checkRec(((Disjunction) node).getLeft()) || checkRec(((Disjunction) node).getRight()));
            } else if (node instanceof Negation) {
                return !checkRec(((Negation) node).get());
            }
        }
        return false;
    }

    private boolean check() {
        return checkRec(expression);
    }

    private void findVar(Expression node) {
        if (node instanceof Variable) {
            vars.add((Variable) node);
        } else {
            for (Expression e : node.getChildren()) {
                findVar(e);
            }
        }
    }

    public Map<Variable, Boolean> find() {
        findVar(expression);
        for (int i = 0; i < Math.pow(2, vars.size()); ++i) {
            int j = 0;
            for (Variable v : vars) {
                boolean value = ((1 << j) & i) != 0;
                vals.put(v, value);
                j++;
            }
            if (!check()) {
                return vals;
            }
        }
        return null;
    }

    public List<Expression> retVars() {
        return new ArrayList<>(vars);
    }
}
