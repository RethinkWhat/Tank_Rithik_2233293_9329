/**
 * Author: TANK, Rithik
 *
 * Sample Run (Done synchronously)
 *
 * TERMINAL 1
 * rithiktank@Rithiks-MBP ~ % telnet localhost 2020
 * Trying 127.0.0.1...
 * Connected to localhost.
 * Escape character is '^]'.
 * What is your name?
 * Rithik
 * What is your age?
 * 45
 * Rithik, you may exercise your right to vote!
 * Thank you and have a good day.
 * Connection closed by foreign host.
 * rithiktank@Rithiks-MBP ~ %
 *
 * TERMINAL 2
 * rithiktank@Rithiks-MBP ~ % telnet localhost 2020
 * Trying 127.0.0.1...
 * Connected to localhost.
 * Escape character is '^]'.
 * What is your name?
 * jelo
 * What is your age?
 * 23
 * jelo, you may exercise your right to vote!
 * Thank you and have a good day.
 * Connection closed by foreign host.
 * rithiktank@Rithiks-MBP ~ %
 */

package pexer2;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PreExercise2 class
 */
public class PreExercise2 implements Runnable{

    // Create ServerSocket object
    ServerSocket serverSocket;

    // Create ExecutorService
    ExecutorService executor;

    /**
     * Main method
     * @param args : String
     */
    public static void main(String[] args) {
        // Create PreExercise2 object
        PreExercise2 preExercise2Obj = new PreExercise2();
        preExercise2Obj.run();
    }

    /**
     * @return void
     *
     * Algorithm:
     *      1. Specify port
     *      2. Define the ExecutorService object thread pool
     *      2. Create a try catch
     *          2.1. Create server sockets, passing in the specified port
     *          2.2. Create a non-terminating loop and pass in the createTask method to submit a new task to Executor object
     */
    public void run() {
        int port = 2000;
        ////Create ExecutorService object with size 5 thread pool
        executor = Executors.newFixedThreadPool(5);

        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                executor.submit(new createTask());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return void
     *
     * Algorithm:
     *      1. Create a try catch with resources
     *          1.1. Create client socket, passing in the specified port to server socket
     *          1.2. Create stream reader and writers
     *      2. Ask client name using streamWriter
     *      3. Accept client name using streamReader
     *      4. Ask and accept client age using streamWriter and streamReader
     *          5.1. Validate age
     *      5. Display a message to client contingent on their age indicating whether they can vote
     */
     class createTask implements Runnable {
         public void run() {
             try (
                     //Create server socket and client socket
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
                         if (age <= 0)
                             throw new NumberFormatException();
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

             } catch (IOException ex) {
                 ex.printStackTrace();
             }
         }
     }
}
