package parser;

import java.util.List;

public class TokenStream {
    private List<Token> operations;
    private int index = 0;

    public Token next() {
        return operations.get(index++);
    }

    public Token prev() {
        return operations.get(--index);
    }

    public void add(Token t) {
        operations.add(t);
    }
}
