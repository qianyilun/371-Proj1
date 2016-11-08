// Done for part 1

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Allen on 2016-10-02.
 */

public class WebServer {
    public static void main(String[] args) throws Exception{
        // set the port number
        int port = 6789;

        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            // Listen for a TCP connection request
            Socket connectionSocket = welcomeSocket.accept();

            // addressing the request
            addressing(connectionSocket);
        }
    }

    private static void addressing(Socket socket) {
        HttpRequest httpRequest = new HttpRequest(socket);

        Thread thread = new Thread(httpRequest);

        thread.start();
    }


}
