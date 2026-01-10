package dev.lauta.httpfromtcp.request;

import dev.lauta.httpfromtcp.header.Header;

public class Request {
    private RequestLine requestLine;
    private Header header;


    public Request(RequestLine requestLine, Header header) {
        this.requestLine = requestLine;
        this.header = header;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Header getHeader() {
        return header;
    }

}
