package pexer3;

// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// IO Imports
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

// XML Parser Import
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Net imports
import java.net.Socket;

// Util Imports
import java.util.ArrayList;
import java.util.List;


/**
 * PreExercise3Client class
 */
public class PreExercise3Client implements Runnable{

    // Class variable to hold the client socket
    Socket client;

    // Variable to hold the lines read in the xml
    int lineRead = 0;


    DocumentBuilder builder;
    Document document;
    NodeList nodeList;


    /**
     * Constructor
     * @param client
     * @throws IOException
     */
    public PreExercise3Client(Socket client) throws IOException{
        this.client = client;
    }

    /**
     * Method to read line from server
     * @return lineRead
     */
    public String readFromServer() {
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
     * Method to write line to server
     * @param line : String
     * @throws IOException
     */
    public void writeToServer(String line) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
        writer.println(line);
    }

    /**
     * Method to get the client socket
     * @return
     */
    public Socket getClient() {
        return client;
    }

    /**
     * Method to set the client socket
     * @param client : Socket
     */
    public void setClient(Socket client) {
        this.client = client;
    }

    /**
     * Method to read line from file
     * @return boolean lineRead
     * @throws IOException
     *
     * Algorithm:
     *      1. Get the node at variable lineRead
     *      2. If the node is null tell the server all the lines have been read
     *          2.1. Return true so the loop that calls this method knows the reading has been complete
     *      3. Get the children node of the chosen node
     *          3.1. Iterate through children node and add the text content of each node to a list
     *      4. Get all the elements in the list and concatenate them to a single string and write to server
     */
    public boolean readLineFromFile() throws IOException {
        List<String> expression;
        Node first = nodeList.item(lineRead);
        if (first == null) {
            writeToServer("bye");
            return true;
        }
        NodeList children = first.getChildNodes();
        expression = new ArrayList<>();
        for (int y = 0; y < children.getLength(); y++) {
            if (children.item(y).getNodeType() == Node.ELEMENT_NODE) {
                expression.add(children.item(y).getTextContent());
            }
        }
        lineRead++;
        String toReturn = "";
        for (String term : expression) {
            toReturn += term + ",";
        }
        writeToServer(toReturn);
        return false;
    }

    /**
     * Method to handle constructing the document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     *
     * Algorithm:
     *      1. Create a documentBuilder object
     *      2. Create a Document object passing in the exer3.xml file
     *      3. Normalize the elements of the document
     *      4. Create a nodeList, getting the elements by the tag, "expression."
     */
    public void constructDocument() throws IOException, ParserConfigurationException, SAXException {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse("res/exer3.xml");
        document.getDocumentElement().normalize();

        nodeList = document.getElementsByTagName("expression");
    }


    /**
     * Run method to handle receiving and sending from client
     *
     * Algorithm:
     *     1. Call the constructDocument method
     *     2. Create a while loop
     *          2.1. Call readLineFromFile() and if return value is true break out of loop
     *          2.2. Read line from server and print line
     */
    public void run() {
        String serverResponse = "";
        boolean finished;
        try {
            constructDocument();
            while (true) {
                finished = readLineFromFile();
                if (finished)
                    break;
                serverResponse = readFromServer();
                System.out.println(serverResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main method to run a client object
     * @param args : String[]
     */
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 2020);

            PreExercise3Client client = new PreExercise3Client(clientSocket);
            client.run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
