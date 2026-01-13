package dev.lauta.httpfromtcp.request;

import dev.lauta.httpfromtcp.body.Body;
import dev.lauta.httpfromtcp.header.Header;

public class Request {
    private RequestLine requestLine;
    private Header header;
    private Body body;


    public Request(RequestLine requestLine, Header header, Body body) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

}
