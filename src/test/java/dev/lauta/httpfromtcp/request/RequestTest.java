package dev.lauta.httpfromtcp.request;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RequestTest {

    @Test
    public void testPrint() {
        //Arrange: Set up the necessary data and context.
        Request request = new Request();

        //Act: Invoke the method or function you want to test.
        String result = request.printRequest("hello");

        //Assert: Verify the expected outcome or behavior.
        assertEquals("hello", result);
    }
}
