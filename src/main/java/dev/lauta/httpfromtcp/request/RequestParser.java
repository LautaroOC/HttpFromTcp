package dev.lauta.httpfromtcp.request;

public class RequestParser {
    private String httpVersion;
    private String requestTarget;
    private String method;

    public RequestParser() {

    }

    public Request checkRequest(String request) {

        //GET /coffee HTTP/1.1\r\n
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
        RequestLine lineParse = new RequestLine(httpVersion, requestTarget, method);
        return new Request(lineParse);
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
