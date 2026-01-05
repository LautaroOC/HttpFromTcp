package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {
    private DatagramSocket datagramSocket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() {
        try {
            datagramSocket = new DatagramSocket(42069);
            running = true;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                datagramSocket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String ACK = "OK";
            byte[] response = ACK.getBytes();
            DatagramPacket packetSend = new DatagramPacket(response, response.length, address, port);
            String message = new String(packet.getData(), 0, packet.getLength());
            if (message.equals("end")) {
                try {
                    datagramSocket.send(packetSend);
                    running = false;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
