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

    public String getHttpVersion() {
        return HttpVersion;
    }

    public String getRequestTarget() {
        return RequestTarget;
    }

    public String getMethod() {
        return Method;
    }
}
