package pexer1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * PreExercise1 class
 */
public class PreExercise1 {
    public static void main(String[] args)  {
        try {
            PreExercise1 preExercise1Obj = new PreExercise1();
            preExercise1Obj.run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Run Method
     * @throws IOException
     * Algorithm:
     *      1. Create an iterator variable
     *      2. Create a reader object
     *      3. Instantiate validation variables
     *      4. Create a do while statement to run while user does not want to exit
     *      5. Display iterator value
     *      5. Prompt user to input a hostname and accept input
     *      6. Call the getAllByName method of the InetAddress class and pass in the host to get all the associated adresses
     *      7. Display the number of addresses
     *      8. Display the host - addresses pair
     *      9. Ask user if they would like to do another search
     */
    public void run() throws IOException {
        int iterator = 1; // Used to store the number of hosts - IP addresses viewed
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Used in validation of continuing program
        char condition;
        boolean validateSearch;

        do{
            System.out.print("Host " + iterator  + " - Type IP address/Hostname: ");
            String host = reader.readLine();
            InetAddress[] addresses = InetAddress.getAllByName(host);

            System.out.println("\tNumber of Hosts/IPs: " + addresses.length );
            System.out.printf("\t%s  %s\n", "Host name", "IP Address");

            // Display each host - IP address pair
            for (InetAddress address : addresses) {
                System.out.printf("\t%s  %s\n",
                        host,
                        // Split the value to get only the IP address and ignore host name
                        address.toString().split("/")[1]);
            }

            // Validation input is Y or N
            do {
                System.out.print("Search another [y/n]? ");
                condition = reader.readLine().charAt(0);
                validateSearch = condition != 'n' && condition != 'y' &&
                condition != 'N' && condition != 'Y';

                // If input is not Y or N prompt user to reenter
                if (validateSearch)
                    System.out.println("Enter [y/n].");
            } while (validateSearch);
            iterator++;
            System.out.println();
        } while (condition != 'n' && condition != 'N');
    }

}
