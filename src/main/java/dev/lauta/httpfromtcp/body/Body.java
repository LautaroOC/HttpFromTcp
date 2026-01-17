package dev.lauta.httpfromtcp.body;

public class Body {
    private byte[] content;
    private int currentPosition = 0;

    public Body(int contentLength) {
        content = new byte[contentLength];
    }

    public int parse(byte[] bytes, int bytesToRead) {
        int cont = 0;
        for (int i = 0; i < bytesToRead; i ++) {
           content[currentPosition] = bytes[i];
           currentPosition++;
           cont++;
        }
        return cont;
    }

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return content.length;
    }


}
