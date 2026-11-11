package main.java;

import java.io.*;

public class CaesarCipher {
    private final FileService files;

    public CaesarCipher(FileService files) {
        this.files = files;
    }

    public void encrypt(String inPath, String outPath, char key) throws IOException {
        try (InputStream in = files.openInput(inPath);
             OutputStream out = new CaesarOutputStream(files.openOutput(outPath), key)) {
            in.transferTo(out);
        }
    }
    public void decrypt(String inPath, String outPath, char key) throws IOException {
        try (InputStream in = new CaesarInputStream(files.openInput(inPath), key);
             OutputStream out = files.openOutput(outPath)) {
            in.transferTo(out);
        }
    }
}
