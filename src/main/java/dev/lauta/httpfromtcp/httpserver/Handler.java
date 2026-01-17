package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.request.Request;

public interface Handler {
    HandlerResult handle(Request request);
}
