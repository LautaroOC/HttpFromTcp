package dev.lauta.httpfromtcp.tcp;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread{

    private File file;
    private BlockingQueue blockingQueue;
    private int port = 42069;
    ServerSocket serverSocket;
    private Boolean running = false;

    public Server(File file, BlockingQueue blockingQueue) {
        this.file = file;
        this.blockingQueue = blockingQueue;
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
                    getLinesChannel(in);
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

    public void getLinesChannel(InputStream inputStream) throws IOException{
        byte[] bytes = new byte[8];
        int bytesRead;
        String text;
        String currentLine = "";
        while ((bytesRead = inputStream.read(bytes)) != -1) {
            byte[] currentRead = new byte[bytesRead];
            for (int i = 0; i < bytesRead; i++) {
                currentRead[i] = bytes[i];
            }
            text = new String(currentRead, StandardCharsets.UTF_8);

            String[] parts = text.split("\n");
            for (int i = 0; i < parts.length; i++) {
                currentLine = currentLine.concat(parts[i]);
                if ((parts.length != 1) && (i != parts.length - 1)) {
                    blockingQueue.add(currentLine);
                    currentLine = "";
                }
            }
        }
        if (!currentLine.isEmpty()) {
            blockingQueue.add(currentLine);
        }
    }
}
