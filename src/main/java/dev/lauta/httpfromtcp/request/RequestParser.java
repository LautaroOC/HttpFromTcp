package dev.lauta.httpfromtcp.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestParser {
    private String httpVersion;
    private String requestTarget;
    private String method;
    private boolean done = false;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] buffer = new byte[16];
    private int bytesRead = 0;
    private int totalBytesRead = 0;
    private int bytesParsed = 0;

    public RequestParser() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    public Request RequestFromReader(InputStream in) {

        while (!done) {
            try {
                bytesRead = in.read(buffer);
                totalBytesRead += bytesRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parseRequest(buffer);
        }

        return checkRequest();
    }

    public Request checkRequest() {

        if (byteArrayOutputStream.toString().isEmpty()) {
            throw new IllegalArgumentException("Empty request");
        }

        String request = byteArrayOutputStream.toString();

        String[] parts = request.split("[ \\r\\n]+");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid request");
        }

        method = parts[0];
        if (!method.equals("GET")) {
            throw new IllegalArgumentException("Invalid method");
        }

        requestTarget = parts[1];
        if (!requestTarget.startsWith("/")) {
            throw new IllegalArgumentException("Invalid target");
        }

        httpVersion = parts[2];
        if (httpVersion.contains("/")) {
            String[] httpParts = parts[2].split("/");
            if (!httpParts[1].equals("1.1")) {
                throw new IllegalArgumentException("Invalid HttpVersion");
            }
        }
        RequestLine lineParse = new RequestLine(httpVersion, requestTarget, method);
        return new Request(lineParse);
    }

    public int parseRequest(byte[] buffer) {
        if (done) {
            return 0;
        }
        int position = containsCRLF(buffer);
        if (position > 0) {
            bytesParsed += position;
            done = true;
            byteArrayOutputStream.write(buffer, 0, position);
            return bytesParsed;
        } else {
            bytesParsed += bytesRead;
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        return bytesParsed;
    }

    public int containsCRLF(byte[] buff) {

        int position = 0;

        for (int i = 0; i < buff.length - 1; i++) {
            if (buff[i] == '\r' && buff[i + 1] == '\n') {
                position = i;
                return position;
            }
        }
        return position;
    }

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

}
