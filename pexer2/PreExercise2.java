package pexer2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PreExercise2 class
 */
public class PreExercise2{
    /**
     * Main method
     * @param args : String
     */
    public static void main(String[] args) {

        // Create PreExercise2 object
        PreExercise2 preExercise2Obj = new PreExercise2();

        //Create ExecutorService object with size 10 thread pool
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Submit the run method of the PreExercise2 object to the executor
        executor.submit(preExercise2Obj.run());
        executor.shutdown();
    }

    /**
     * @return Runnable object
     *
     * Algorithm:
     *      1. Specify port
     *      2. Create a try catch with resources
     *          2.1. Create server and client sockets, passing in the specified port to server socket
     *          2.2. Create stream reader and writers
     *      3. Ask client name using streamWriter
     *      4. Accept client name using streamReader
     *      5. Ask and accept client age using streamWriter and streamReader
     *          5.1. Validate age
     *      6. Display a message to client contingent on their age indicating whether they can vote
     */
    public Runnable run() {
        int port = 2000;
        while (true) {
            try (
                    //Create server socket and client socket
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();

                    // Create stream reader and writers to accept input and send output to client
                    BufferedReader streamReader = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter streamWriter = new PrintWriter(new OutputStreamWriter(
                            clientSocket.getOutputStream()), true);
                    ) {

                // Server sends message to client
                streamWriter.println("What is your name? ");

                // Server accepts input from client
                String name = streamReader.readLine();
                int age;
                // While user input isn't a valid integer value we can consider as age
                while (true) {
                    streamWriter.println("What is your age? ");
                    try {
                        age = Integer.parseInt(streamReader.readLine());
                        if (age <= 0 )
                            throw  new NumberFormatException();
                        else
                            break;
                    } catch (NumberFormatException nfe) {
                        streamWriter.println("Please enter a valid age.");
                        continue;
                    }
                }
                // Age contingent output message
                if (age >= 18)
                    streamWriter.println(name + ", you may exercise your right to vote! ");
                else
                    streamWriter.println(name + ", you are still too young to vote! ");

                streamWriter.println("Thank you and have a good day.");

            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
