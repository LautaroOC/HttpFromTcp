package udp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {
    private DatagramSocket datagramSocket;
    private int port;
    private InetAddress address;

    public Client() {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        DatagramPacket packet;
        byte[] buffMes = message.getBytes();
        try {
            address = InetAddress.getByName("localhost");
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        packet = new DatagramPacket(buffMes, buffMes.length, address, 42069);
        try {
            datagramSocket.send(packet);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] mesRec = new byte[256];
        DatagramPacket packetRec = new DatagramPacket(mesRec, mesRec.length);
        try {
            datagramSocket.receive(packetRec);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        String messageReceived = new String(packetRec.getData(), 0, packetRec.getLength());
        System.out.println(messageReceived);
        datagramSocket.close();
    }

}
