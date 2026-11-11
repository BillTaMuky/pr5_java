package main.java;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public class CaesarInputStream extends FilterInputStream {
    private final int shift;

    public CaesarInputStream(InputStream in, char key) {
        super(in);
        this.shift = key;
    }

    @Override
    public int read() throws IOException {
        int b = in.read();
        return (b == -1) ? -1 : (b - shift) & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        for (int i = off; i < off + Math.max(n, 0); i++) {
            b[i] = (byte) ((b[i] - shift) & 0xFF);
        }
        return n;
    }
}

