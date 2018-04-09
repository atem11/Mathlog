package axioms;

import operations.Expression;

public class AxiomChecker {
    private static Equaler[] axiomsChecker = new Equaler[11];

    static {
        for (int i = 1; i < 11; ++i) {
            axiomsChecker[i] = new Equaler(ClassicalAxioms.axioms[i]);
        }
    }

    public static boolean check(Expression exp, int i) {
        return axiomsChecker[i].check(exp);
    }

    public static int checkAll(Expression exp) {
        for (int i = 1; i < 11; ++i) {
            if (check(exp, i)) {
                return i;
            }
        }
        return 0;
    }
}
