package dev.lauta.httpfromtcp.response;

public enum WriteStatus {
    STATUSLINE,
    HEADERS,
    BODY,
    TRAILER,
    WRITE
}
