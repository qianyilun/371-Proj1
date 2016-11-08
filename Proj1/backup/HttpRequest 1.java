// Done for part 1
import java.io.*;
import java.net.Socket;

/**
 * Created by Allen on 2016-10-02.
 */
final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
    private Socket socket;

    public HttpRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception {
        // socket's input and output streams
        // create input stream, attached to socket
        BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        // create output stream, attached to socket
        DataOutputStream outToClient = new DataOutputStream(
                socket.getOutputStream());

        String reqestLine = inFromClient.readLine();

        System.out.println();
        System.out.println(reqestLine);

        // Get and display the header lines
        String headerLine = null;
        while ((headerLine = inFromClient.readLine()).length() != 0) {
            System.out.println(headerLine);
        }

        // close streams and socket
        outToClient.close();
        inFromClient.close();
        socket.close();
    }
}
