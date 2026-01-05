package udp;

import java.net.DatagramSocket;

public class MainUdp {
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client();

        server.start();
        client.sendMessage("end");
    }
}
