import java.io.*;
import java.net.*;

public class ClientSocket {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Simulate a request for memory allocation
            String request = "request memory";
            out.println(request);
            System.out.println("Sent to server: " + request);

            // Receive response from the server
            String response = in.readLine();
            System.out.println("Received from server: " + response);

            // Simulate a check for memory status
            request = "check memory";
            out.println(request);
            System.out.println("Sent to server: " + request);

            // Receive and print server response
            response = in.readLine();
            System.out.println("Received from server: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
