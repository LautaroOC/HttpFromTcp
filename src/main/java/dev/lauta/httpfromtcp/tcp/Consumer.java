package dev.lauta.httpfromtcp.tcp;
import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {

    private BlockingQueue blockingQueue;

    public Consumer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void run(){
        while(this.isAlive()) {
            try {
                consumer();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void consumer() throws InterruptedException {
        String line = blockingQueue.take().toString();
        System.out.println("read: " + line);
    }
}
