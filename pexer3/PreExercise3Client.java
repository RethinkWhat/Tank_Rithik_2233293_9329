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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PreExercise3Client implements Runnable{

    Socket client;

    static int clientID =0;

    int lineRead = 0;



    public PreExercise3Client(ServerSocket sever) throws IOException{
        this.client = sever.accept();
        clientID +=1;
    }

    public void readFromServer() {
        String nextLine = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            nextLine = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(nextLine);
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
            readFromServer();

    }
}
