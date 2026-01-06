package dev.lauta.httpfromtcp.udp;

public class MainUdp {
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client();

        server.start();
        client.sendMessage("end");
    }
}
