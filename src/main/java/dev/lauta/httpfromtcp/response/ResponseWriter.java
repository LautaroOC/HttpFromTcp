package dev.lauta.httpfromtcp.response;

import dev.lauta.httpfromtcp.header.Header;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ResponseWriter {
    private OutputStream outputStream;
    private String statusLine;
    private Header header;
    private byte[] body;
    private WriteStatus writeStatus = WriteStatus.STATUSLINE;

    public ResponseWriter(OutputStream out) {
        this.outputStream = out;
        this.header = new Header();
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void setStatusLine(StatusCode statusCode) throws IOException {
        if (writeStatus == WriteStatus.STATUSLINE) {
            if (statusCode == StatusCode.OK) {
                statusLine = "HTTP/1.1 200 OK\r\n";
            } else if (statusCode == StatusCode.BAD_REQUEST) {
                statusLine = "HTTP/1.1 400 Bad Request\r\n";
            } else if (statusCode == StatusCode.INTERNAL_SERVER_ERROR) {
                statusLine = "HTTP/1.1 500 Internal Server Error\r\n";
            } else {
                throw new IllegalArgumentException("Unsupported status code\r\n");
            }
            writeStatus = WriteStatus.HEADERS;
        }
    }

    public void setDefaultHeaders(int contentLen) {
        if (writeStatus == WriteStatus.HEADERS) {
            //ArrayList<String> contentLengthValue = new ArrayList<>();
            //contentLengthValue.add(String.valueOf(contentLen));
            //header.put("Content-Length", contentLengthValue);
            ArrayList<String> connectionValue = new ArrayList<>();
            connectionValue.add("close");
            header.put("Connection", connectionValue);

            writeStatus = WriteStatus.BODY;
        }
    }

    public void setBody(byte[] body) throws IOException {
        if (writeStatus == WriteStatus.BODY) {
            this.body = body;
            writeStatus = WriteStatus.WRITE;
        }
    }

    public void writeHeaders() throws IOException {
        for (Map.Entry<String, ArrayList<String>> entry : header.entrySet()) {
            for (String value : entry.getValue()) {
                String line = (entry.getKey() + ": " + value + "\r\n");
                outputStream.write(line.getBytes(StandardCharsets.UTF_8));
            }
        }
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public void writeResponse() throws IOException {
        if (writeStatus == WriteStatus.WRITE) {
            outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
            writeHeaders();
            outputStream.write(body);
        }
    }

    public void setHeader(String name, String value) {
        ArrayList<String> values = new ArrayList<>();
        values.add(value);
        header.put(name, values);
    }

    public void WriteChunkedBody(byte[] p) throws IOException{
        int pLength = p.length;
        String sizeHex = Integer.toHexString(pLength);
        String line = sizeHex + "\r\n";
        outputStream.write(line.getBytes(StandardCharsets.US_ASCII));

        outputStream.write(p);
        outputStream.write(("\r\n").getBytes(StandardCharsets.US_ASCII));
    }

    public void WriteChunkedBodyDone() throws IOException{
        String line ="0\r\n";
        outputStream.write(line.getBytes(StandardCharsets.US_ASCII));
        outputStream.write(("\r\n").getBytes(StandardCharsets.US_ASCII));
        outputStream.flush();
    }


}
