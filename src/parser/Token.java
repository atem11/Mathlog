package parser;

public class Token {
    enum tokens {
        L_BRACE, R_BRACE, VARIABLE,
        CONJUNCTION, DISJUNCTION,
        IMPLICATION, NEGATION, END
    }

    private tokens token;
    private String name;

    Token(String s, String e) {
        name = e;
        switch (s) {
            case "#":
                token = tokens.END;
                break;
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

    public String getName() {
        return name;
    }
}
