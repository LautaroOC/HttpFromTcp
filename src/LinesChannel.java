import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class LinesChannel extends Thread{

    private File file;
    private BlockingQueue blockingQueue;

    public LinesChannel(File file, BlockingQueue blockingQueue) {
        this.file = file;
        this.blockingQueue = blockingQueue;
    }

    public void run() {
       getLinesChannel();
    }

    public void getLinesChannel() {
        byte[] bytes = new byte[8];
        int bytesRead;
        String text;
        String currentLine = "";
        try(FileInputStream fileInputStream = new FileInputStream(file)) {

            while((bytesRead = fileInputStream.read(bytes)) != -1) {

                byte[] currentRead = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++) {
                    currentRead[i] = bytes[i];
                }
                text = new String(currentRead, StandardCharsets.UTF_8);

                String[] parts = text.split("\n");
                for (int i = 0; i < parts.length; i++) {
                    currentLine = currentLine.concat(parts[i]);
                    if ((parts.length != 1) && (i != parts.length -1)){
                        blockingQueue.add(currentLine);
                        currentLine = "";
                    }
                }
            }
            if (!currentLine.isEmpty()) {
                blockingQueue.add(currentLine);
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
    }
}
