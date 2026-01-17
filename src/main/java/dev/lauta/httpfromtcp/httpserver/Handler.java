package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.response.ResponseWriter;

public interface Handler {
    void handle(ResponseWriter rw, Request request);
}
