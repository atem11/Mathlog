package parser;

import operations.Expression;

public class Token {
    private enum tokens {
        L_BRACE, R_BRACE, VARIABLE,
        CONJUNCTION, DISJUNCTION,
        IMPLICATION, NEGATION
    }

    private tokens token;
    private Expression exp;

    public Token(String s, Expression e) {
        exp = e;
        switch (s) {
            case "(":
                token = tokens.L_BRACE;
                break;
            case ")":
                token = tokens.R_BRACE;
                break;
            case "!":
                token = tokens.NEGATION;
                break;
            case "->":
                token = tokens.IMPLICATION;
                break;
            case "|":
                token = tokens.DISJUNCTION;
                break;
            case "&":
                token = tokens.CONJUNCTION;
                break;
            default:
                token = tokens.VARIABLE;
        }
    }

    public tokens get() {
        return token;
    }
}
