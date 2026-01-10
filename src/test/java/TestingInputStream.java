import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestingInputStream extends InputStream {

    private byte[] requestBytes;
    private int currentPosition = 0;
    private int randomBytesToRead;
    private int bytesLeft;
    private int bytesToRead;

    public TestingInputStream(String request) {
        requestBytes = request.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException(
                "Please use the read(byte[], int, int) method"
        );
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (b.length == 0) {
            return 0;
        }
        if (currentPosition >= requestBytes.length) {
            return -1;
        }
        randBytesChunks();
        bytesToRead = Math.min(randomBytesToRead, b.length);
        bytesToRead = Math.min(bytesToRead, bytesLeft);
        for (int i = 0; i < bytesToRead; i++) {
            b[i] = requestBytes[currentPosition];
            currentPosition++;
        }
        return bytesToRead;
    }

    public void randBytesChunks() {
        bytesLeft = requestBytes.length - currentPosition;
        randomBytesToRead = 1 + (int) (Math.random() * bytesLeft);
    }

    public int getBytesToRead() {
        return bytesToRead;
    }

}
