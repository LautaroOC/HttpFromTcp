package dev.lauta.httpfromtcp.httpserver;
import  dev.lauta.httpfromtcp.response.StatusCode;

public class HandlerResult {
    private final StatusCode statusCode;
    private final String body;

    public HandlerResult(StatusCode status, String body) {
        this.statusCode = status;
        this.body = body;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
