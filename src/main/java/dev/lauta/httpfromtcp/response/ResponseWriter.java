package dev.lauta.httpfromtcp.response;

import dev.lauta.httpfromtcp.header.Header;
import jdk.jfr.internal.util.Output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;


public class ResponseWriter {
    private Writer writer;
    private OutputStream outputStream;

    public ResponseWriter(OutputStream out) {
        this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        this.outputStream = out;
    }

    public void flush() throws IOException{
        writer.flush();
        outputStream.flush();
    }

    public void writeStatusLine(StatusCode statusCode) throws IOException {
        if (statusCode == StatusCode.OK) {
            writer.write("HTTP/1.1 200 OK\r\n");
        }
        else if (statusCode == StatusCode.BAD_REQUEST) {
            writer.write("HTTP/1.1 400 Bad Request\r\n");
        }
        else if (statusCode == StatusCode.INTERNAL_SERVER_ERROR) {
            writer.write("HTTP/1.1 500 Internal Server Error\r\n");
        }
        else {
            throw new IllegalArgumentException("Unsupported status code\r\n");
        }
    }

    public Header getDefaultHeaders(int contentLen) {
        Header header = new Header();
        ArrayList<String> contentLengthValue = new ArrayList<>();
        contentLengthValue.add(String.valueOf(contentLen));
        header.put("Content-Length", contentLengthValue);
        ArrayList<String> connectionValue = new ArrayList<>();
        connectionValue.add("close");
        header.put("Connection", connectionValue);

        return header;
    }

    public void writeBody(byte[] body) throws IOException{
        outputStream.write(body);
    }

}
