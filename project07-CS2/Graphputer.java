import java.io.*;
import java.net.*;
import java.util.*;

public class Graphputer {
    private  final String GRAPHPUTER = "labradoodle.caltech.edu";
    private Socket SOCKET = null;
    private PrintWriter TO_GRAPHPUTER = null;
    private BufferedReader FROM_GRAPHPUTER = null;
    private boolean ready = false;

    public Graphputer() throws FileNotFoundException {
        try {
            Scanner portFile = new Scanner(new File("port.txt"));
            int port = Integer.parseInt(portFile.next());
            SOCKET = new Socket(GRAPHPUTER, port);
            TO_GRAPHPUTER = new PrintWriter(SOCKET.getOutputStream(), true);
            FROM_GRAPHPUTER = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));
            System.out.println("You can view the mmio output using: http://labradoodle.caltech.edu:" + (port * 2));
        } catch (IOException e) {
            ready = false;
        }
        ready = true;
    }

    private void cleanupGraphputer() {
        try {
            TO_GRAPHPUTER.close();
            FROM_GRAPHPUTER.close();
            SOCKET.close();
        } catch(Exception e) {
            /* Ignore cleanup issues. */
        }
    }

    private void writeToGraphputer(String cmd) {
        TO_GRAPHPUTER.write(cmd.trim() + "\n");
        TO_GRAPHPUTER.flush();
    }

    private String readFromGraphputer() {
        try {
            String resp = FROM_GRAPHPUTER.readLine().trim();
            if (resp.equals("Bye.")) {
                return null;
            }
            return resp;
        } catch (IOException e) {
            return null;
        }
    }

    public String run(String cmd) {
        if (!ready) {
            return null;
        }
        writeToGraphputer(cmd);
        return readFromGraphputer();
    }
}
