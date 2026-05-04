import java.io.*;
import java.net.*;

public class SocketServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Waiting for client messages...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected.");

                    // Read data from client (process IDs and allocated memory)
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received from client: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
