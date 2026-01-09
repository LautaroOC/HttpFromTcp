package dev.lauta.httpfromtcp.request;

public class Request {
    private RequestLine requestLine;


    public Request(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

}
