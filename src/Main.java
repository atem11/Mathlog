import operations.Expression;
import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.TokenStream;

public class  Main {
    public static void main(String[] args) {
        String S = "P->!QQ->!R10&S|!T&U&V";
        Lexer lex = new Lexer();
        TokenStream str = lex.lexer(S);
        for (int i = 0; i < str.size(); ++i) {
            Token t = str.next();
            System.out.println(t.get() + "\t" + t.getName());
        }
        str.reset();
        Parser parser = new Parser();
        Expression root = parser.parce(str);
        System.out.println(root.toTree());
    }
}
