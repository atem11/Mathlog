package proofer;

import operations.*;
import proofer.Lems.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Proof {
    private List<Expression> vars;
    private Expression expression;
    private List<Expression> proof = new ArrayList<>();

    public Proof(List<Expression> var, Expression e) {
        vars = var;
        expression = e;
    }

    private boolean eval(Map<Variable, Boolean> vars, Expression exp) {
        if (exp instanceof Variable) {
            return vars.get(exp);
        } else if (exp instanceof Negation) {
            return !eval(vars, ((Negation) exp).get());
        } else if (exp instanceof Implication) {
            return !eval(vars, ((Implication) exp).getLeft()) || eval(vars, ((Implication) exp).getRight());
        } else if (exp instanceof Conjunction) {
            return eval(vars, ((Conjunction) exp).getLeft()) && eval(vars, ((Conjunction) exp).getRight());
        } else if (exp instanceof Disjunction) {
            return eval(vars, ((Disjunction) exp).getLeft()) || eval(vars, ((Disjunction) exp).getRight());
        }
        return true;
    }

    public List<Expression> gen(Map<Variable, Boolean> var, Expression exp) {
        List<Expression> proof = new ArrayList<>();

        if (exp instanceof Conjunction) {
            Conjunction op = (Conjunction) exp;
            proof.addAll(gen(var, op.getLeft()));
            proof.addAll(gen(var, op.getRight()));
            boolean lVal = eval(var, op.getLeft());
            boolean rVal = eval(var, op.getRight());
            if (!lVal && !rVal) {
                proof.addAll(Conj_FF.proof(op.getLeft(), op.getRight()));
            } else if (!lVal) {
                proof.addAll(Conj_FT.proof(op.getLeft(), op.getRight()));
            } else if (!rVal) {
                proof.addAll(Conj_TF.proof(op.getLeft(), op.getRight()));
            } else {
                proof.addAll(Conj_TT.proof(op.getLeft(), op.getRight()));
            }
        } else if (exp instanceof Disjunction) {
            Disjunction op = (Disjunction) exp;
            proof.addAll(gen(var, op.getLeft()));
            proof.addAll(gen(var, op.getRight()));
            boolean lVal = eval(var, op.getLeft());
            boolean rVal = eval(var, op.getRight());
            if (!lVal && !rVal) {
                proof.addAll(Disj_FF.proof(op.getLeft(), op.getRight()));
            } else if (!lVal) {
                proof.addAll(Disj_FT.proof(op.getLeft(), op.getRight()));
            } else if (!rVal) {
                proof.addAll(Disj_TF.proof(op.getLeft(), op.getRight()));
            } else {
                proof.addAll(Disj_TT.proof(op.getLeft(), op.getRight()));
            }
        } else if (exp instanceof Implication) {
            Implication op = (Implication) exp;
            proof.addAll(gen(var, op.getLeft()));
            proof.addAll(gen(var, op.getRight()));
            boolean lVal = eval(var, op.getLeft());
            boolean rVal = eval(var, op.getRight());
            if (!lVal && !rVal) {
                proof.addAll(Impl_FF.proof(op.getLeft(), op.getRight()));
            } else if (!lVal) {
                proof.addAll(Impl_FT.proof(op.getLeft(), op.getRight()));
            } else if (!rVal) {
                proof.addAll(Impl_TF.proof(op.getLeft(), op.getRight()));
            } else {
                proof.addAll(Impl_TT.proof(op.getLeft(), op.getRight()));
            }
        } else if (exp instanceof Negation) {
            Negation op = (Negation) exp;
            proof.addAll(gen(var, op.get()));
            boolean val = eval(var, op.get());
            if (val) {
                proof.addAll(Neg_T.proof(op.get()));
            } else {
                proof.addAll(Neg_F.proof(op.get()));
            }
        } else {
            Variable op = (Variable) exp;
            boolean val = eval(var, op);
            proof.add(val ? op : new Negation(op));
        }
        return proof;

    }

    private List<Expression> merge(List<Expression> varA, List<Expression> a, List<Expression> varB,  List<Expression> b) {
        List<Expression> deductA = Deductor.makeProofToRight(varA.get(varA.size() - 1), a, varA);
        List<Expression> deductB = Deductor.makeProofToRight(varB.get(varB.size() - 1), b, varB);
        List<Expression> ans = deductA;
        ans.addAll(deductB);

        return ans;
    }

    public List<Expression> genProof() {
        proof = new ArrayList<>();


        return proof;
    }
}
