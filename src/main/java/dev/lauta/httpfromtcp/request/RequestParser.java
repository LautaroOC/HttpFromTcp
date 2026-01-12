package dev.lauta.httpfromtcp.request;

import dev.lauta.httpfromtcp.header.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestParser {
    private int bytesParsed = 0;
    private byte lastByteOccurence;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private byte[] buffer = new byte[16];
    private int bytesRead = 0;
    private int totalBytesRead = 0;
    private boolean lineReady = false;
    private ParserState state = ParserState.REQUEST_LINE;
    private RequestLine requestLine;
    private Header header;

    public RequestParser() {
    }

    public Request RequestFromReader(InputStream in) {

        requestLine = null;
        header = new Header();

        while (state != ParserState.DONE) {
            try {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    String line = byteArrayOutputStream.toString();
                    processLine(line);
                    break;
                }
                totalBytesRead += bytesRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parseRequest(buffer);
        }
        return new Request(requestLine, header);
    }

    byte lastByte;

    public int parseRequest(byte[] buffer) {


        if (state == ParserState.DONE) {
            return 0;
        }
        int start = 0;

        for (int i = 0; i < bytesRead; i++) {

            byte b = buffer[i];

            if (b == '\n' && lastByte == '\r') {
                String line = byteArrayOutputStream.toString();
                processLine(line);
                byteArrayOutputStream.reset();
            } else if (b != '\r') {
                byteArrayOutputStream.write(b);
            }
            lastByte = b;
        }
        return bytesParsed;
    }

    public void processLine(String line) {
        if (state == ParserState.REQUEST_LINE) {
            requestLine = RequestLine.parse(line);
            state = ParserState.HEADER_LINE;
        } else if (state == ParserState.HEADER_LINE) {
            if (line.isEmpty()) {
                state = ParserState.DONE;
            } else {
                header.parse(line);
            }
        }
    }

}
