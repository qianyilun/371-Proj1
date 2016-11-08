// 合并全部，着手修改InputStream, DataOutputStream
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by Allen on 2016-10-02.
 */

public final class WebServer {
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
        // Construct an object to process the HTTP request message
        try {
            HttpRequest httpRequest = new HttpRequest(socket);
            
            // Create a new thread to process the request
            Thread thread = new Thread(httpRequest);

            // Start the thread
            thread.start();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        // begin part 2
    }
}




/**
 * Created by Allen on 2016-10-02.
 */
final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;

    public HttpRequest(Socket socket) throws Exception {
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

        String requestLine = inFromClient.readLine();

        System.out.println();
        System.out.println("requestLine is: ");
        System.out.println(requestLine);

        System.out.println();

        // part 2
        stageTwo(requestLine, outToClient);



        // Get and display the header lines
//        String headerLine = null;
//        System.out.println("headerLine is: ");
//        while ((headerLine = inFromClient.readLine()).length() != 0) {
//            System.out.println(headerLine);
//        }

        // close streams and socket
        outToClient.close();
        inFromClient.close();
        socket.close();
    }

    private void stageTwo(String requestLine, DataOutputStream outToClient){
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); // should be "GET"
        String fileName = tokens.nextToken();
        fileName = "." + fileName;
        System.out.println("fileName is: " + fileName);

        // Open the requested file
        FileInputStream file = null;
        boolean fileExists = true;

        try {
            file = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
            System.out.println("File NOT found!");
        }

        // Construct the response message
        responseMessage(fileExists, fileName, outToClient, file);

    }

    private void responseMessage(boolean fileExists, String fileName, DataOutputStream outToClient,
                                 FileInputStream file) {
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        if (fileExists) {
            statusLine = "HTTP/1.1 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType( fileName ) + CRLF;
        }
        else {
            statusLine = "HTTP/1.1 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: " + contentType( ".html" ) + CRLF;
            entityBody = "<HTML>" +
                    "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                    "<BODY>Not Found</BODY></HTML>";
        }

        // Write to Client dataOutputStream
        try {
            outToClient.writeBytes(statusLine);
            outToClient.writeBytes(contentTypeLine);
            outToClient.writeBytes(CRLF);

        } catch (IOException e) {
            System.out.println("Fail to write!");
            e.printStackTrace();
        }

        // Send the entity body.
        try {
            if (fileExists) {
                sendBytes(file, outToClient);
                file.close();
            } else {
                outToClient.writeBytes(entityBody);
            }
        } catch (IOException e) {
            System.out.println("File failed to close!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("File failed to sendBytes!");
            e.printStackTrace();
        }

    }

    private String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".gif")) {
            return "imge/gif";
        }

        if (fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        // file extension is unknown:
        return "application/octet-stream";
    }

    private static void sendBytes(FileInputStream file, OutputStream outToClient)
            throws Exception {

        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while((bytes = file.read(buffer)) != -1 ) {
            outToClient.write(buffer, 0, bytes);
        }
    }
}
