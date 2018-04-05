package parser;

import operations.*;

public class Parser {
    private TokenStream stream;

    public Expression parce(TokenStream str) {
        stream = str;
        return implication();
    }

    private Expression implication() {
        Expression first = disjunction();
        while (true) {
            Token t = stream.next();
            switch (t.get()) {
                case IMPLICATION:
                    first = new Implication(first, implication());
                    break;
                default:
                    stream.prev();
                    return first;
            }
        }
    }

    private Expression disjunction() {
        Expression first = conjunction();
        while (true) {
            Token t = stream.next();
            switch (t.get()) {
                case DISJUNCTION:
                    first = new Disjunction(first, conjunction());
                    break;
                default:
                    stream.prev();
                    return first;
            }
        }
    }

    private Expression conjunction() {
        Expression first = atomix();
        while (true) {
            Token t = stream.next();
            switch (t.get()) {
                case CONJUNCTION:
                    first = new Conjunction(first, atomix());
                    break;
                default:
                    stream.prev();
                    return first;
            }
        }
    }

    private Expression atomix() {
        Token t = stream.next();
        switch (t.get()) {
            case VARIABLE:
                return new Variable(t.getName());
            case NEGATION:
                return new Negation(atomix());
            case L_BRACE:
                Expression tmp = implication();
                stream.next();
                return tmp;
            default:
                return null;

        }
    }
}
