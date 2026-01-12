package dev.lauta.httpfromtcp.tcp;
import dev.lauta.httpfromtcp.header.Header;
import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.request.RequestParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread{

    private int port = 42069;
    ServerSocket serverSocket;
    private Boolean running = false;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        running = true;
        while (running) {
            try {
                acceptConnection();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void acceptConnection() throws IOException {
        while (true) {
            if (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection accepted");
                    InputStream in = clientSocket.getInputStream();
                    RequestParser requestParser = new RequestParser();
                    Request request = requestParser.RequestFromReader(in);
                    Header header = request.getHeader();

                    System.out.println("Request line:");
                    System.out.println("- Method: " + request.getRequestLine().getMethod());
                    System.out.println("- Target: " + request.getRequestLine().getRequestTarget());
                    System.out.println("- Version: " + request.getRequestLine().getHttpVersion());
                    System.out.println("- HEADERS:");
                    for (String i : header.keySet()) {
                        System.out.println("- " + i.toUpperCase() + ": " + header.get(i));
                    }


                } catch (IOException e) {
                    if (running) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else {
                serverSocket.close();
            }
        }
    }

}
