package dev.lauta.httpfromtcp.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.nio.charset.StandardCharsets;

public class Server extends Thread {

    private int port = 42069;
    ServerSocket serverSocket;
    private Boolean running = false;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        running = true;
        while (running) {
            try {
                acceptConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void acceptConnection() throws IOException {
        try {
            Socket clientSocket = serverSocket.accept();
            handler(clientSocket);

        } catch (IOException e) {
            if (running) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handler(Socket clientSocket) {
        try {
            OutputStream out = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: 13\n" +
                    "\n" +
                    "Hello World!";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
