package dev.lauta.httpfromtcp.response;

import dev.lauta.httpfromtcp.header.Header;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;


public class Response {

    public Response() {

    }

    public static void writeStatusLine(Writer w, StatusCode statusCode) throws IOException {
        if (statusCode == StatusCode.OK) {
            w.write("HTTP/1.1 200 OK\r\n");
        }
        else if (statusCode == StatusCode.BAD_REQUEST) {
            w.write("HTTP/1.1 400 Bad Request\r\n");
        }
        else if (statusCode == StatusCode.INTERNAL_SERVER_ERROR) {
            w.write("HTTP/1.1 500 Internal Server Error\r\n");
        }
        else {
            throw new IllegalArgumentException("Unsupported status code\r\n");
        }
    }

    public static Header getDefaultHeaders(int contentLen) {
        Header header = new Header();
        ArrayList<String> contentLengthValue = new ArrayList<>();
        contentLengthValue.add(String.valueOf(contentLen));
        header.put("Content-Length", contentLengthValue);
        ArrayList<String> connectionValue = new ArrayList<>();
        connectionValue.add("close");
        header.put("Connection", connectionValue);
        ArrayList<String> contentTypeValue = new ArrayList<>();
        contentTypeValue.add("text/plain");
        header.put("Content-Type", contentTypeValue);
        return header;
    }

    public static void writeHeaders(Writer w, Header header) throws IOException {
        for (Map.Entry<String, ArrayList<String>> entry : header.entrySet()) {
            for (String value : entry.getValue()) {
                w.write(entry.getKey() + ": " + value + "\r\n");
            }
        }
        w.write("\r\n");
    }

}
