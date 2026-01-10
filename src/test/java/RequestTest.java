import dev.lauta.httpfromtcp.header.Header;
import org.junit.Test;
import dev.lauta.httpfromtcp.request.Request;
import dev.lauta.httpfromtcp.request.RequestLine;
import dev.lauta.httpfromtcp.request.RequestParser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RequestTest {

    //GOOD


    @Test
    public void testForTheTestingClass01() {
        String raw = "GET / HTTP/1.1\r\n";
        TestingInputStream testingInputStream = new TestingInputStream(raw);
        byte[] buff = new byte[16];
        int bytesRead;

        //Act: Invoke the method or function you want to test.
        try {
            bytesRead = testingInputStream.read(buff);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Assert: Verify the expected outcome or behavior.
        assertEquals(testingInputStream.getBytesToRead(), bytesRead);
    }

    @Test
    public void testForTheTestingClass02() {
        String raw = "GET / HTTP/1.1\r\n";
        TestingInputStream testingInputStream = new TestingInputStream(raw);
        byte[] buff = new byte[0];
        int bytesRead;

        //Act: Invoke the method or function you want to test.
        try {
            bytesRead = testingInputStream.read(buff);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Assert: Verify the expected outcome or behavior.
        assertEquals(0, bytesRead);
    }




    @Test
    public void testGoodRequest() {
        //Arrange: Set up the necessary data and context.
        String raw = "GET / HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        TestingInputStream testingInputStream = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();
        RequestLine requestLine;
        Request request;

        //Act: Invoke the method or function you want to test.
        request = requestParser.RequestFromReader(testingInputStream);
        requestLine = request.getRequestLine();

        //Assert: Verify the expected outcome or behavior.
        assertEquals("GET", requestLine.getMethod());
        assertEquals("/", requestLine.getRequestTarget());
        assertEquals("HTTP/1.1", requestLine.getHttpVersion());
    }


  //  @Test
    //public void testGoodRequestWithPath() {
        //Arrange: Set up the necessary data and context.
        //String raw = "GET /coffee HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
     /*   TestingInputStream testingInputStream = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();
        Request request;
        RequestLine requestLine;

        //Act: Invoke the method or function you want to test.
        request = requestParser.RequestFromReader(testingInputStream);
        requestLine = request.getRequestLine();


        //Assert: Verify the expected outcome or behavior.
        assertEquals("GET", requestLine.getMethod());
        assertEquals("/coffee", requestLine.getRequestTarget());
        assertEquals("HTTP/1.1", requestLine.getHttpVersion());
    }

    @Test
    public void testValidSingleHeader() {
        String raw = "Host: localhost:42069\r\n\r\n";
        TestingInputStream testingInputStream = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();
        Request request;
        Header header;

        request = requestParser.RequestFromReader(testingInputStream);
        header = request.getHeader();

        assertEquals("Host", header.findKey("Host"));
        assertEquals("localhost:42069", header.findValue("localhost:42069"));

    }

    //BAD
    @Test
    public void testInvalidParts() {
        //Arrange: Set up the necessary data and context.
        String raw = "coffee HTTP/2.1";
        TestingInputStream in = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exRequest = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.RequestFromReader(in);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid request", exRequest.getMessage());
    }

    @Test
    public void testInvalidMethod() {
        //Arrange: Set up the necessary data and context.

      */
       // String raw = "/coffee GET HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        /*TestingInputStream in = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exceptionMethod = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.RequestFromReader(in);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid method", exceptionMethod.getMessage());
    }

    @Test
    public void testInvalidVersion() {
        //Arrange: Set up the necessary data and context.

         */
       // String raw = "GET /coffee HTTP/2.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        /*TestingInputStream in = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exceptionVersion = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.RequestFromReader(in);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid HttpVersion", exceptionVersion.getMessage());
    }

    @Test
    public void testEmptyRequest() {
        String raw = "";
        TestingInputStream in = new TestingInputStream(raw);
        RequestParser requestParser = new RequestParser();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.RequestFromReader(in);
        });

        assertEquals("Empty request", ex.getMessage());
    }
         */
}
