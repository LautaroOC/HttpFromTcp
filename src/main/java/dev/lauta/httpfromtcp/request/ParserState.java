package dev.lauta.httpfromtcp.request;

public enum ParserState {
    REQUEST_LINE,
    HEADER_LINE,
    DONE
}
