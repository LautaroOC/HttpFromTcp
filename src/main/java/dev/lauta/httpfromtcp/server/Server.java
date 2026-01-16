package dev.lauta.httpfromtcp.server;

import dev.lauta.httpfromtcp.header.Header;
import dev.lauta.httpfromtcp.response.Response;
import dev.lauta.httpfromtcp.response.StatusCode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import java.nio.charset.StandardCharsets;

public class Server extends Thread {

    private int port = 42069;
    ServerSocket serverSocket;
    private Boolean running = false;
    private StatusCode statusCode;

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
            statusCode = StatusCode.OK;
            handler(clientSocket);

        } catch (IOException e) {
            if (running) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handler(Socket clientSocket) {
        try {
            Writer w = new OutputStreamWriter(
                    clientSocket.getOutputStream(),
                    StandardCharsets.UTF_8
            );
            Response.writeStatusLine(w, statusCode);
            Header header = Response.getDefaultHeaders(0);
            Response.writeHeaders(w, header);
            w.flush();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
