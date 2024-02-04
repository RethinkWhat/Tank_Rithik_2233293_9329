package pexer3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class PreExercise3Server implements Runnable {

    public ServerSocket server;


    public PreExercise3Server() throws IOException {
        server = new ServerSocket(2000);
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

    @Override
    public void run() {
        try {
            server = new ServerSocket(2000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String solveExpression(String toSolve) {
        double solved;
        String[] expression = toSolve.split(",");

        if (expression.length != 3) {
            return null; // Return null for invalid expressions
        }

        try {
            switch (expression[1]) {
                case "+" -> solved = Double.valueOf(expression[0]) + Double.valueOf(expression[2]);
                case "-" -> solved = Double.valueOf(expression[0]) - Double.valueOf(expression[2]);
                case "%" -> solved = Double.valueOf(expression[0]) % Double.valueOf(expression[2]);
                case "/" -> solved = Double.valueOf(expression[0]) / Double.valueOf(expression[2]);
                case "*" -> solved = Double.valueOf(expression[0]) * Double.valueOf(expression[2]);
                case "^" -> solved = Math.pow(Double.valueOf(expression[0]), Double.valueOf(expression[2]));
                default -> {
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return expression[0] + " " + expression[1] + " " + expression[2] + " = " + solved;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        int iterator = 0;
        try {
            PreExercise3Server server = new PreExercise3Server();
            while (true) {
                PreExercise3Client client = new PreExercise3Client(server.getServer());

                // Iterate while there are lines being sent to server
                do {

                    client.readLineFromFile();


                    // Read the expression

                    Thread thread1 = new Thread(() -> {
                        // Read expression from client
                        String expression = readFromClient(client.getClient());


                        // Solve expression from server
                        String solvedExpression = solveExpression(expression);


                        // Write solution to client
                        writeToClient(client.getClient(), solvedExpression);

                        // Let client read solved solution
                        client.readFromServer();

                    });
                    thread1.start();

                    iterator++;
                } while (iterator != 10);


            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

