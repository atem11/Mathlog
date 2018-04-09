import operations.Expression;
import parser.Lexer;
import parser.Parser;
import parser.TokenStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hw0 {
    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);

        StringBuilder all_line = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            all_line.append(line);
        }

        Lexer lex = new Lexer();
        TokenStream str = lex.lexer(all_line.toString());
        Parser parser = new Parser();
        Expression root = parser.parse(str);
        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            out.println(root.toTree());
        }
        reader.close();

    }
}
