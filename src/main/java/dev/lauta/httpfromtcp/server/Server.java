package dev.lauta.httpfromtcp.server;

import dev.lauta.httpfromtcp.header.Header;
import dev.lauta.httpfromtcp.httpserver.Handler;
import dev.lauta.httpfromtcp.httpserver.HandlerResult;
import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.request.RequestParser;
import dev.lauta.httpfromtcp.response.Response;
import dev.lauta.httpfromtcp.response.StatusCode;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server extends Thread {

    Handler handler;
    ServerSocket serverSocket;
    private Boolean running = false;
    private StatusCode statusCode;

    public Server(int port, Handler handler) {
        this.handler = handler;
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
            handle(clientSocket);
        } catch (IOException e) {
            if (running) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handle(Socket clientSocket) throws IOException {
        InputStream in = clientSocket.getInputStream();
        RequestParser requestParser = new RequestParser();
        Request request = requestParser.RequestFromReader(in);

        HandlerResult handlerResult = handler.handle(request);

        Writer w = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
        Response.writeStatusLine(w, handlerResult.getStatusCode());

        byte[] bodyBytes = handlerResult.getBody().getBytes(StandardCharsets.UTF_8);
        Header header = Response.getDefaultHeaders(bodyBytes.length);
        Response.writeHeaders(w, header);

        w.write(handlerResult.getBody());
        w.flush();
        clientSocket.close();
    }

}
