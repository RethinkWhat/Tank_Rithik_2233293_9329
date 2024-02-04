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

public class PreExercise3Client implements Callable {

    Socket client;
    BufferedReader reader;
    PrintWriter writer;

    static int clientID =0;



    public PreExercise3Client(ServerSocket sever) throws IOException{
        this.client = sever.accept();
        clientID +=1;
    }

    public String readFromServer() throws IOException {
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String nextLine =  reader.readLine();
        reader.close();
        return nextLine;
    }


    public void write(String line) throws IOException {
        writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.println(line);
        writer.close();
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public List<List<String>> call() throws IOException {
        List<List<String>> expressionsList = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("res/exer3.xml");
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("expression");

            for (int x =0 ; x < nodeList.getLength(); x++ ) {
                Node first = nodeList.item(x);
                NodeList children = first.getChildNodes();
                List<String> expression = new ArrayList<>();
                for (int y = 0; y< children.getLength(); y++) {
                    if (children.item(y).getNodeType() == Node.ELEMENT_NODE) {
                        expression.add(children.item(y).getTextContent());
                    }
                }
                expressionsList.add(expression);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return expressionsList;
    }
}
