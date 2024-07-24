import java.io.*;
import java.net.*;
import java.util.*;

public class Repl {
    public static void main(String[] args) throws FileNotFoundException {
        Graphputer g = new Graphputer();

        Scanner console = new Scanner(System.in);
        while (true) {
            System.out.print("graphputer> ");
            try {
                String result = g.run(console.nextLine());
                if (result == null) {
                    System.out.println("graphputer disconnected.");
                    break;
                }
                System.out.println(result);
            } catch (NoSuchElementException e) {
                /* cleanup and exit below */
                break;
            }
        }
    }
}
