package pexer3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PreExercise3Server class
 */
public class PreExercise3Server {

    // ServerSocket class variable
    private ServerSocket server;

    /**
     * Class Construct
     * @param port : int
     * @throws IOException
     */
    public PreExercise3Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    /**
     * Method to read String from a client
     * @param client : Socket
     * @return lineRead
     *
     * Algorithm:
     *      1. Create a reader object and pass in client input stream
     *      2. Call the readLine method of the reader object and return the value
     */
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

    /**
     * Method to write String to client
     * @param client : Socket
     * @param line : String
     * @return void
     *
     * Algorithm:
     *      1. Create a writer object and pass in client output stream
     *      2. Call the println method of the writer object
     */
    public static void writeToClient(Socket client, String line) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
            writer.println(line);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Getter of the server object
     * @return server
     */
    public ServerSocket getServer() {
        return server;
    }

    /**
     * Setter of the server object
     * @param server : ServerSocket
     */
    public void setServer(ServerSocket server) {
        this.server = server;
    }

    /**
     * Method to solve an expression given it is passed us a comma seperated String
     * @param toSolve : String
     * @return solvedExpression
     *
     * Algorithm:
     *      1. Split the passed in expression using the regex ','
     *      2. If the expression length is not exactly 3 return null for error
     *      3. Otherwise, create a switch statement that looks at the operator
     *          3.1. Depending on the string representation of the operator perform the appropriate solving
     *          3.2. If operator passed in is not a valid operator, return a message indicating as such
     *          3.3. If solving is successful, return the formatted expression and the result
     */
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

    /**
     * Main method to handle running the server
     *
     * Algorithm:
     *      1. Create a PreExercise3Server obj
     *      2. Accept a connection for the serverSocket
     *      3. Create a thread which will handle running the PreExercise3Client obj
     *      3. Create a while loop
     *          3.1. Read a line from client using the readFromClient method
     *          3.2. If the value of the line read is "bye," exit loop
     *          3.3. Otherwise, call the solveExpression method on the passed in line
     *          3.4. Send the solved expression to Client using the writeToClient method
     * @param args
     */
    public static void main(String[] args) {
        PreExercise3Server server;
        Socket client = null;
        String expressionToSolve = "";

        try {
            server = new PreExercise3Server(2020);

            client = server.getServer().accept();
            new Thread(new PreExercise3Client(client));

            while (true) {
                expressionToSolve = readFromClient(client);
                if (expressionToSolve.equals("bye"))
                    break;
                writeToClient(client, solveExpression(expressionToSolve));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

