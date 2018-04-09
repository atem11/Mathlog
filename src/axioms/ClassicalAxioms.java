package axioms;

import operations.Expression;
import parser.Lexer;
import parser.Parser;

public class ClassicalAxioms {
    public static String[] textOfAxioms = new String[] {
            "",                             //(0)
            "A->B->A",                      //(1)
            "(A->B)->(A->B->C)->(A->C)",    //(2)
            "A->B->A&B",                    //(3)
            "(A&B)->A",                     //(4)
            "(A&B)->B",                     //(5)
            "A->(A|B)",                     //(6)
            "B->(A|B)",                     //(7)
            "(A->C)->(B->C)->((A|B)->C)",   //(8)
            "(A->B)->(A->(!B))->(!A)",      //(9)
            "(!!A)->A"                      //(10)

    };

    public static Expression[] axioms = new Expression[11];

    static {
        Parser p = new Parser();
        Lexer l = new Lexer();
        for (int i = 1; i < 11; ++i) {
            axioms[i] = p.parce(l.lexer(textOfAxioms[i]));
        }
    }
}
