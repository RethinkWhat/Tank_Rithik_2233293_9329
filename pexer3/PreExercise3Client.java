package pexer3;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.ls.LSOutput;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PreExercise3Client implements Runnable{

    Socket client;

    int lineRead = 0;



    public PreExercise3Client(Socket client) throws IOException{
        this.client = client;
    }

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


    public void writeToServer(String line) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
        writer.println(line);
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void readLineFromFile() throws IOException {
        List<String> expression = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("res/exer3.xml");
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("expression");

            Node first = nodeList.item(lineRead);
            if (first == null) {
                writeToServer("done");
                return;
            }
            NodeList children = first.getChildNodes();
            expression = new ArrayList<>();
            for (int y = 0; y < children.getLength(); y++) {
                if (children.item(y).getNodeType() == Node.ELEMENT_NODE) {
                    expression.add(children.item(y).getTextContent());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        lineRead++;
        String toReturn = "";
        for (String term: expression) {
            toReturn += term +",";
        }
        writeToServer(toReturn);
    }


    public void run() {
        String serverResponse = "";
        try {
            while (!serverResponse.equalsIgnoreCase("bye")) {
                readLineFromFile();
                serverResponse = readFromServer();
                System.out.println(serverResponse);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
