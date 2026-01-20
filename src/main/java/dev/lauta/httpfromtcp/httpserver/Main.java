package dev.lauta.httpfromtcp.httpserver;

import dev.lauta.httpfromtcp.header.Header;
import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.response.ResponseWriter;
import dev.lauta.httpfromtcp.response.StatusCode;
import dev.lauta.httpfromtcp.server.Server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


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
                    rw.setHeader("Transfer-Encoding", "chunked");
                    rw.setHeader("Trailer", "X-Content-SHA256, X-Content-Length");
                    rw.setDefaultHeaders(html.getBytes(StandardCharsets.UTF_8).length);
                    //rw.setBody(html.getBytes(StandardCharsets.UTF_8));
                    rw.writeStatusLineAndHeaders();
                    rw.flush();
                    int chunked = 3;
                    int startIndex = 0;
                    byte[] htmlResponse = html.getBytes(StandardCharsets.UTF_8);
                    int length = 0;
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    while (startIndex < htmlResponse.length) {

                        int end = Math.min(startIndex + chunked, htmlResponse.length);
                        byte[] part = Arrays.copyOfRange(htmlResponse, startIndex, end);
                        startIndex = end;

                        rw.writeChunkedBody(part);
                        digest.update(part);
                        length += part.length;
                    }
                    byte[] hash = digest.digest();
                    rw.writeChunkedBodyDone();
                    Header trailers = new Header();
                    ArrayList<String> hashArray = new ArrayList<>();
                    hashArray.add(toHex(hash));
                    trailers.put("X-Content-SHA256", hashArray);
                    ArrayList<String> xContentLength = new ArrayList<>();
                    xContentLength.add(Integer.toString(length));
                    trailers.put("X-Content-Length", xContentLength);

                    rw.writeTrailers(trailers);

                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Server server = new Server(42069, handler);
        server.start();
    }

    private static String toHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

}


