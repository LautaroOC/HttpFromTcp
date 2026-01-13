package dev.lauta.httpfromtcp.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestLine {
    private String HttpVersion;
    private String RequestTarget;
    private String Method;


    public RequestLine(String httpVersion, String requestTarget, String method) {
        this.HttpVersion = httpVersion;
        this.RequestTarget = requestTarget;
        this.Method = method;
    }


    public String getHttpVersion() {
        return HttpVersion;
    }

    public String getRequestTarget() {
        return RequestTarget;
    }

    public String getMethod() {
        return Method;
    }

    public static RequestLine parse(String request) {

        String method;
        String requestTarget;
        String httpVersion;


        if (request.isEmpty()) {
            throw new IllegalArgumentException("Empty request");
        }

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

        return new RequestLine(httpVersion, requestTarget, method);
    }

}
