package parser;

public class Lexer {

    public TokenStream lexer(String s) {
        TokenStream str = new TokenStream();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isWhitespace(s.charAt(i))) {
                continue;
            }
            switch (s.charAt(i)) {
                case '(':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token("(", "("));
                    break;
                case ')':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token(")", ")"));
                    break;
                case '-':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token("->", "->"));
                    i++;
                    break;
                case '|':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token("|", "|"));
                    break;
                case '&':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token("&", "&"));
                    break;
                case '!':
                    if (name.length() != 0) {
                        str.add(new Token("v", name.toString()));
                        name = new StringBuilder();
                    }
                    str.add(new Token("!", "!"));
                    break;
                default:
                    name.append(s.charAt(i));
                    break;

            }
        }
        if (name.length() != 0) {
            str.add(new Token("v", name.toString()));
        }
        str.add(new Token("#", "#"));
        return str;
    }
}
