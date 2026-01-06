package dev.lauta.httpfromtcp.request;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RequestTest {

    //GOOD
    @Test
    public void testGoodRequest(){
        //Arrange: Set up the necessary data and context.
        String raw = "GET / HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        requestParser.checkRequest(raw);

        //Assert: Verify the expected outcome or behavior.
        assertEquals("GET", requestParser.getMethod());
        assertEquals("/", requestParser.getRequestTarget());
        assertEquals("HTTP/1.1", requestParser.getHttpVersion());
    }

    @Test
    public void testGoodRequestWithPath(){
        //Arrange: Set up the necessary data and context.
        String raw = "GET /coffee HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        requestParser.checkRequest(raw);

        //Assert: Verify the expected outcome or behavior.
        assertEquals("GET", requestParser.getMethod());
        assertEquals("/coffee", requestParser.getRequestTarget());
        assertEquals("HTTP/1.1", requestParser.getHttpVersion());
    }

    //BAD
    @Test
    public void testInvalidParts() {
        //Arrange: Set up the necessary data and context.
        String raw = "coffee HTTP/2.1";
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exRequest = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.checkRequest(raw);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid request", exRequest.getMessage());
    }

    @Test
    public void testInvalidMethod() {
        //Arrange: Set up the necessary data and context.
        String raw = "/coffee GET HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exceptionMethod = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.checkRequest(raw);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid method", exceptionMethod.getMessage());
    }

    @Test
    public void testInvalidVersion() {
        //Arrange: Set up the necessary data and context.
        String raw = "GET /coffee HTTP/2.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        RequestParser requestParser = new RequestParser();

        //Act: Invoke the method or function you want to test.
        IllegalArgumentException exceptionVersion = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.checkRequest(raw);
        });

        //Assert: Verify the expected outcome or behavior.
        assertEquals("Invalid HttpVersion", exceptionVersion.getMessage());
    }

    @Test
    public void testEmptyRequest() {
        String raw = "";
        RequestParser requestParser = new RequestParser();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            requestParser.checkRequest(raw);
        });
        
       assertEquals("Empty request", ex.getMessage());
    }

}
