package main.java;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CaesarOutputStream extends FilterOutputStream {
    private final int shift;

    public CaesarOutputStream(OutputStream out, char key) {
        super(out);
        this.shift = key;
    }

    @Override
    public void write(int b) throws IOException {
        out.write((b + shift) & 0xFF);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }
}
