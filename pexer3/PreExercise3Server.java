package pexer3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PreExercise3Server implements Runnable {

    public ServerSocket server;

    BufferedReader reader;
    PrintWriter writer;

    public PreExercise3Server() throws IOException {
        server = new ServerSocket(2000);
    }

    public String read(Socket client) throws IOException {
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String nextLine = reader.readLine();
        reader.close();
        return nextLine;
    }

    public void write(Socket client, String line) throws IOException {
        writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.println(line);
        writer.close();
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

    public static Callable solveExpression(List<String> expression) {
        return new Callable() {
            @Override
            public List<String> call() {
                double solved;
                try {
                    switch (expression.get(1)) {
                        case "+" -> solved = Double.valueOf(expression.get(0)) + Double.valueOf(expression.get(2));
                        case "-" -> solved = Double.valueOf(expression.get(0)) - Double.valueOf(expression.get(2));
                        case "%" -> solved = Double.valueOf(expression.get(0)) % Double.valueOf(expression.get(2));
                        case "/" -> solved = Double.valueOf(expression.get(0)) / Double.valueOf(expression.get(2));
                        case "*" -> solved = Double.valueOf(expression.get(0)) * Double.valueOf(expression.get(2));
                        case "^" -> solved = Math.pow(Double.valueOf(expression.get(0)), Double.valueOf(expression.get(2)));
                        default -> {
                            return null;
                        }
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
                expression.add(String.valueOf(solved));
                return expression;
            }
        };
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try {
            PreExercise3Server server = new PreExercise3Server();
            while (true) {
                PreExercise3Client client = new PreExercise3Client(server.getServer());

                // The list of expressions
                List<List<String>> expressionsList = (List<List<String>>) client.call();

                //The list of solved expressions
                List<List<String>> solvedExpressionsList = new ArrayList<>();
                ;

                // Loop through all the expressions
                for (List<String> expression : expressionsList) {

                    try {
                        // Solve all the expressions
                        List<String> message = (List<String>) solveExpression(expression).call();
                        server.write(client.getClient(),message.toString());
                    } catch (Exception ex) {
                    }
                    System.out.println("");

                }

            }

            //   System.out.println(expressions.get(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

