import axioms.AxiomChecker;
import operations.Expression;
import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.TokenStream;

public class  Main {
    public static void main(String[] args) {
        String S = "(A->B)->((A->B)|B)";
        Lexer lex = new Lexer();
        TokenStream str = lex.lexer(S);
        Parser parser = new Parser();
        Expression root = parser.parce(str);
        System.out.println(root.toTree());
        System.out.println(AxiomChecker.checkAll(root));
    }
}
