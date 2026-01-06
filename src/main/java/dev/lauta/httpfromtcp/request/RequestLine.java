package dev.lauta.httpfromtcp.request;

public class RequestLine {
    private String HttpVersion;
    private String RequestTarget;
    private String Method;

    public RequestLine(String httpVersion, String requestTarget, String method) {
        this.HttpVersion = httpVersion;
        this.RequestTarget = requestTarget;
        this.Method = method;
    }

}
