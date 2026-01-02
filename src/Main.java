import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        File file = new File("src/messages.txt");
        readFile(file);

    }

    public static void readFile(File file) {
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
                       System.out.println("read: " + currentLine);
                        currentLine = "";
                    }
                }
            }
            if (!currentLine.isEmpty()) {
                System.out.println("read: " + currentLine);
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
    }
}