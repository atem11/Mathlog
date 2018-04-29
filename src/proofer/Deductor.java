package proofer;

import axioms.AxiomChecker;
import operations.Expression;
import operations.Implication;

import java.util.*;

public class Deductor {

    public static List<Expression> makeProofToRight(Expression alpha, List<Expression> proof, List<Expression> hyp) {
        List<Expression> ans = new ArrayList<>();
        Set<Expression> truth = new HashSet<>();
        Map<Expression, List<Expression>> deductNotTrue = new HashMap<>();
        Map<Expression, Expression> deductTrue = new HashMap<>();
        for (Expression line : proof) {
            if (line instanceof Implication) {
                Implication impl = (Implication) line;
                Expression a = impl.getLeft();
                Expression b = impl.getRight();
                if (!truth.contains(a)) {
                    deductNotTrue.computeIfAbsent(b, k -> new ArrayList<>());
                    deductNotTrue.get(b).add(a);
                } else {
                    deductNotTrue.put(b, null);
                    deductTrue.put(b, a);
                }
            }
            if (hyp.contains(line) || AxiomChecker.checkAll(line) != 0) {
                ans.add(line);
                ans.add(new Implication(line, new Implication(alpha, line)));
                ans.add(new Implication(alpha, line));
            } else if (line.equals(alpha)) {
                Expression e5 = new Implication(line, line);
                Expression e1 = new Implication(line, e5);
                Expression e4 = new Implication(line, new Implication(e5, line));
                Expression e3 = new Implication(e4, e5);
                Expression e2 = new Implication(e1, e3);
                ans.add(e1);
                ans.add(e2);
                ans.add(e3);
                ans.add(e4);
                ans.add(e5);
            } else {
                Expression prof = null;
                if (!deductTrue.containsKey(line)) {
                    List<Expression> mbProof = deductNotTrue.get(line);
                    for (Expression mB : mbProof) {
                        if (truth.contains(mB)) {
                            deductTrue.put(line, mB);
                            deductNotTrue.put(line, null);
                            prof = mB;
                            break;
                        }
                    }
                } else {
                    prof = deductTrue.get(line);
                }
                Expression l3 = new Implication(alpha, line);
                Expression l2 = new Implication(new Implication(alpha, new Implication(prof, line)), l3);
                Expression l1 = new Implication(new Implication(alpha, prof), l2);
                ans.add(l1);
                ans.add(l2);
                ans.add(l3);
            }
            truth.add(line);
        }
        return ans;
    }

    public static List<Expression> makeProofToLeft(List<Expression> proof) {
        Implication last = (Implication) proof.get(proof.size() - 1);
        proof.add(last.getLeft());
        proof.add(last.getRight());
        return proof;
    }

}
