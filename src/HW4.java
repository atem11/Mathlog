import grid.Graph;
import grid.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HW4 {

    private static final int FAIL_CONST = 1337;

    public static void main(String[] args) throws IOException {
        Path pathToInputFile = Paths.get("input.txt");
        BufferedReader reader = Files.newBufferedReader(pathToInputFile);
        int N = Integer.parseInt(reader.readLine());
        Graph graph = new Graph(N);
        for (int i = 0; i < N; ++i) {
            String line = reader.readLine();
            String[] ver = line.split(" ");
            for (String aVer : ver) {
                int to = Integer.parseInt(aVer) - 1;
                graph.add_edge(to, i);
            }
        }

        try (PrintWriter out = new PrintWriter(new File("output.txt"))) {
            Table _test = graph.get_equals();
            boolean FAIL = false;

            Table plus = graph.get_plus();
            for (int i = 0; i < N; i++) {
                if (FAIL) {
                    break;
                }
                for (int j = i + 1; j < N; j++) {
                    if (plus.get(i, j) == FAIL_CONST) {
                        out.println("Операция '+' не определена: " + (i + 1) + "+" + (j + 1));
                        FAIL = true;
                        break;
                    }
                }
            }

            Table mull = graph.get_mull();
            for (int i = 0; i < N; i++) {
                if (FAIL) {
                    break;
                }
                for (int j = i + 1; j < N; j++) {
                    if (mull.get(i, j) == FAIL_CONST) {
                        out.println("Операция '*' не определена: " + (i + 1) + "*" + (j + 1));
                        FAIL = true;
                        break;
                    }
                }
            }

            for (int i = 0; i < N; i++) {
                if (FAIL) {
                    break;
                }
                for (int j = 0; j < N; j++) {
                    if (FAIL) {
                        break;
                    }
                    for (int k = 0; k < N; k++) {
                        if (mull.get(i, plus.get(j, k)) != plus.get(mull.get(i, j), mull.get(i, k))) {
                            out.println("Нарушается дистрибутивность: " + (i + 1) + "*(" + (j + 1) + "+" + (k + 1) + ")");
                            FAIL = true;
                        }
                    }
                }
            }
            Table impl = new Table(1);
            if (!FAIL) {
                impl = graph.get_impl();
            }
            for (int i = 0; i < N; i++) {
                if (FAIL) {
                    break;
                }
                for (int j = i + 1; j < N; j++) {
                    if (impl.get(i, j) == FAIL_CONST) {
                        out.println("Операция '->' не определена: " + (i + 1) + "->" + (j + 1));
                        FAIL = true;
                        break;
                    }
                }
            }

            if (!FAIL) {
                int ZERO = graph.get_zero();
                int ONE = graph.get_one();
                for (int i = 0; i < N; i++) {
                    if (FAIL) {
                        break;
                    }
                    if (plus.get(i, impl.get(i, ZERO)) != ONE) {
                        out.println("Не булева алгебра: " + (i + 1) + "+~" + (i + 1));
                        FAIL = true;
                        break;
                    }
                }
            }

            if (!FAIL) {
                out.println("Булева алгебра");
            }

            //TEEEEEST///////////////////////////////////////////**/
//            _test = plus;
//            out.print("  ");                                    /**/
//            for (int i = 0; i < N; i++) {                       /**/
//                out.print(i + " ");                             /**/
//            }                                                   /**/
//            out.println();                                      /**/
//            for (int i = 0; i < N; i++) {                       /**/
//                out.print(i + ":");                             /**/
//                for (int j = 0; j < N; j++) {                   /**/
//                    if (_test.get(i, j) == FAIL_CONST) {        /**/
//                        out.print("@ ");                        /**/
//                    } else {                                    /**/
//                        out.print(_test.get(i, j) + " ");       /**/
//                    }                                           /**/
//                }                                               /**/
//                out.println();                                  /**/
//            }                                                   /**/
            /////////////////////////////////////////////////////**/
        } catch (IOException ignored) {
        }
    }
}
