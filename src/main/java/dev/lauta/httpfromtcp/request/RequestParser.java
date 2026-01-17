package dev.lauta.httpfromtcp.request;

import dev.lauta.httpfromtcp.body.Body;
import dev.lauta.httpfromtcp.header.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestParser {
    private int bytesParsed = 0;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private ByteArrayOutputStream byteArrayOutputStreamBody = new ByteArrayOutputStream();
    private byte[] buffer = new byte[16];
    private int bytesRead = 0;
    private int bodyBytesRead = 0;
    private int totalBytesRead = 0;
    private byte lastByte;
    private int bodyContentLengthBytes = 0;
    private boolean foundBodyContent = false;
    private boolean foundRequestLineCRLF = false;
    private boolean foundHeaderCRLF = false;
    private ParserState state = ParserState.REQUEST_LINE;
    private RequestLine requestLine;
    private Header header;
    private Body body;

    public RequestParser() {
    }

    public Request RequestFromReader(InputStream in) {

        requestLine = null;
        header = new Header();

        while (state != ParserState.DONE) {
            try {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    if (bodyBytesRead < bodyContentLengthBytes) {
                        throw new IllegalArgumentException("Incomplete body");
                    }
                    if (state == ParserState.BODY_LINE && header.containsKey("content-length") && bodyContentLengthBytes == 0) {
                        processBody(buffer);
                        if (bodyContentLengthBytes != 0 && bodyBytesRead == 0) {
                            throw new IllegalArgumentException("Empty body");
                        }
                    }
                    if (!foundRequestLineCRLF || !foundHeaderCRLF) {
                        String line = byteArrayOutputStream.toString();
                        processLine(line);
                    }

                    break;
                }
                totalBytesRead += bytesRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parseRequest(buffer);
        }
        return new Request(requestLine, header, body);
    }

    public void parseRequest(byte[] buffer) {
        if (state == ParserState.REQUEST_LINE || state == ParserState.HEADER_LINE) {
            containsCRLF(buffer);
        }
        else if (state == ParserState.BODY_LINE) {
            processBody(buffer);
        }
    }


    public int containsCRLF(byte[] buffer) {


        if (state == ParserState.DONE) {
            return 0;
        }

        for (int i = 0; i < bytesRead; i++) {

            byte b = buffer[i];

            if (b == '\n' && lastByte == '\r') {
                String line = byteArrayOutputStream.toString();
                if (i < bytesRead && line.isEmpty()) {
                    processLine(line);
                    byteArrayOutputStream.reset();
                    byteArrayOutputStreamBody.write(buffer, i+1, bytesRead - i - 1);
                    return i;
                }
                else {
                    processLine(line);
                    byteArrayOutputStream.reset();
                }
            } else if (b != '\r') {
                byteArrayOutputStream.write(b);
            }
            lastByte = b;
        }
        return bytesParsed;
    }

    public void processLine(String line) {
        if (state == ParserState.REQUEST_LINE) {
            requestLine = RequestLine.parse(line);
            state = ParserState.HEADER_LINE;
            foundRequestLineCRLF = true;
        } else if (state == ParserState.HEADER_LINE) {
            if (line.isEmpty()) {
                state = ParserState.BODY_LINE;
                foundHeaderCRLF = true;
                if (!header.containsKey("content-length")) {
                    state = ParserState.DONE;
                }
            } else {
                header.parse(line);
            }
        }
    }

    public void processBody(byte[] buffer) {
        String key = "content-length";
        if (!foundBodyContent) {
            if (header.containsKey(key)) {
                ArrayList<String> value = header.get(key);
                bodyContentLengthBytes = Integer.parseInt(value.get(0));
                body = new Body(bodyContentLengthBytes);
                foundBodyContent = true;

                byte[] storedBytes = byteArrayOutputStreamBody.toByteArray();
                if (storedBytes.length > 0) {
                    bodyBytesRead += body.parse(storedBytes, storedBytes.length);
                }
            } else {
                state = ParserState.DONE;
            }
        }
        if (foundBodyContent && bodyContentLengthBytes != 0) {
            if (bodyContentLengthBytes > bodyBytesRead) {
                int bytesToRead = Math.min(bytesRead, bodyContentLengthBytes - bodyBytesRead);
                bodyBytesRead += body.parse(buffer, bytesToRead);
            } else if (bodyContentLengthBytes == bodyBytesRead){
                state = ParserState.DONE;
            }
        }
    }

}
