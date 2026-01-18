package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.request.RequestLine;
import dev.lauta.httpfromtcp.response.ResponseWriter;
import dev.lauta.httpfromtcp.response.StatusCode;
import dev.lauta.httpfromtcp.server.Server;
import dev.lauta.httpfromtcp.httpserver.Handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args) {

        Handler handler = new Handler() {
            @Override
            public void handle(ResponseWriter rw, Request request) {
                String target = request.getRequestLine().getRequestTarget();
                StatusCode statusCode;
                String html400 = "<html>\n" +
                        "  <head>\n" +
                        "    <title>400 Bad Request</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <h1>Bad Request</h1>\n" +
                        "    <p>Your request honestly kinda sucked.</p>\n" +
                        "  </body>\n" +
                        "</html>\n";
                String html500 = "<html>\n" +
                        "  <head>\n" +
                        "    <title>500 Internal Server Error</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <h1>Internal Server Error</h1>\n" +
                        "    <p>Okay, you know what? This one is on me.</p>\n" +
                        "  </body>\n" +
                        "</html>";
                String html200 = "<html>\n" +
                        "  <head>\n" +
                        "    <title>200 OK</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <h1>Success!</h1>\n" +
                        "    <p>Your request was an absolute banger.</p>\n" +
                        "  </body>\n" +
                        "</html>";
                String html;
                try {
                    if (target.equals("/yourproblem")) {
                        statusCode = StatusCode.BAD_REQUEST;
                        html = html400;
                    } else if (target.equals("/myproblem")) {
                        statusCode = StatusCode.INTERNAL_SERVER_ERROR;
                        html = html500;
                    } else {
                        statusCode = StatusCode.OK;
                        html = html200;
                    }

                    rw.setStatusLine(statusCode);
                    rw.setHeader("Content-Type", "text/html");
                    rw.setDefaultHeaders(html.getBytes(StandardCharsets.UTF_8).length);
                    rw.setBody(html.getBytes(StandardCharsets.UTF_8));

                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        };
    Server server = new Server(42069, handler);
    server.start();
    }

}

