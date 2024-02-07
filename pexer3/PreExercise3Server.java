package pexer3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class PreExercise3Server {

    private ServerSocket server;


    public PreExercise3Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public static String readFromClient(Socket client) {
        String nextLine = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            nextLine = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nextLine;
    }

    public static void writeToClient(Socket client, String line) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
            writer.println(line);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public static String solveExpression(String toSolve) {
        double solved;
        String[] expression = toSolve.split(",");

        if (expression.length != 3) {
            return null; // Return null for invalid expressions
        }
        String error = expression[0] + " " + expression[1] + " " +  expression[2] + " = Invalid Expression";

        try {
            switch (expression[1]) {
                case "+" -> solved = Double.valueOf(expression[0]) + Double.valueOf(expression[2]);
                case "-" -> solved = Double.valueOf(expression[0]) - Double.valueOf(expression[2]);
                case "%" -> solved = Double.valueOf(expression[0]) % Double.valueOf(expression[2]);
                case "/" -> solved = Double.valueOf(expression[0]) / Double.valueOf(expression[2]);
                case "*" -> solved = Double.valueOf(expression[0]) * Double.valueOf(expression[2]);
                case "^" -> solved = Math.pow(Double.valueOf(expression[0]), Double.valueOf(expression[2]));
                default -> {
                    return  error;
                }
            }
        } catch (NumberFormatException e) {
            return error;
        }
        return expression[0] + " " + expression[1] + " " + expression[2] + " = " + solved;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        PreExercise3Server server;
        Socket client = null;
        String expressionToSolve = "";

            try {
                server = new PreExercise3Server(2020);

                while (true) {
                    client = server.getServer().accept();
                    new Thread(new PreExercise3Client(client));

                    while (true) {
                        expressionToSolve = readFromClient(client);
                        if (expressionToSolve.equals("done"))
                            break;
                        writeToClient(client, solveExpression(expressionToSolve));
                    }
                    writeToClient(client, "bye");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

    }
}

