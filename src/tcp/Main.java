package tcp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        File file = new File("src/messages.txt");
        BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
        LinesChannel linesChannel = new LinesChannel(file, blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);
        linesChannel.start();
        consumer.start();

    }
}