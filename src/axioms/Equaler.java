package axioms;

import operations.Expression;
import operations.Variable;

import java.util.HashMap;

public class Equaler {
    private HashMap<Variable, Expression> axiomVars;
    private Expression equals;

    public Equaler(Expression pattern) {
        this.equals = pattern;
        axiomVars = new HashMap<>();
    }

    private boolean walkAndCheck(Expression fromAxiom, Expression toCheck) {
        if (fromAxiom instanceof Variable) {
            Variable axiomVariable = (Variable)fromAxiom;
            if (!axiomVars.containsKey(axiomVariable)) {
                axiomVars.put(axiomVariable, toCheck);
                return true;
            } else {
                return axiomVars.get(axiomVariable).equals(toCheck);
            }
        }
        if (!fromAxiom.getSymbol().equals(toCheck.getSymbol())) {
            return false;
        }
        Expression[] axiomChildren = fromAxiom.getChildren();
        Expression[] toCheckChildren = toCheck.getChildren();
        for (int i = 0; i < fromAxiom.getChildrenCount(); i++) {
            if (!walkAndCheck(axiomChildren[i], toCheckChildren[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean check(Expression other) {
        axiomVars.clear();
        return walkAndCheck(equals, other);
    }
}
