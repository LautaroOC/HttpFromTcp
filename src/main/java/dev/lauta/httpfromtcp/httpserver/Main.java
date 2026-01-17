package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.request.RequestLine;
import dev.lauta.httpfromtcp.response.StatusCode;
import dev.lauta.httpfromtcp.server.Server;
import dev.lauta.httpfromtcp.httpserver.Handler;


public class Main{
    public static void main(String[] args) {

        Handler handler = new Handler() {
            @Override
            public HandlerResult handle(Request request) {
                String target = request.getRequestLine().getRequestTarget();

                if (target.equals("/yourproblem")) {
                    String message = "Your problem is not my problem\n";
                    StatusCode status = StatusCode.BAD_REQUEST;
                    return new HandlerResult(status, message);
                } else if (target.equals("/myproblem")) {
                    String message = "Woopsie, my bad\n";
                    StatusCode status = StatusCode.INTERNAL_SERVER_ERROR;
                    return new HandlerResult(status, message);
                } else {
                    String message = "All good frfr\n";
                    StatusCode status = StatusCode.OK;
                    return new HandlerResult(status, message);
                }
            }
        };
        Server server = new Server(42069, handler);
        server.start();
    }
}

