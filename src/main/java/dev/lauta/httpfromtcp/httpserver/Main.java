package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.server.Server;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        server.start();

    }
}
