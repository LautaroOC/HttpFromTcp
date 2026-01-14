package dev.lauta.httpfromtcp.server;

public enum ConnectionState {
    ACCEPTED,
    READING_REQUEST,
    WRITING_RESPONSE,
    CLOSED
}
