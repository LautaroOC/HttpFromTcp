package dev.lauta.httpfromtcp.tcp;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        File file = new File("src/messages.txt");
        BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
        Server server = new Server(file, blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);
        server.start();
        consumer.start();

    }
}