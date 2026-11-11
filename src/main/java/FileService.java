package main.java;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public void writeText(String path, String text) throws IOException {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(text);
        }
    }


    public InputStream openInput(String path) throws IOException {
        return new BufferedInputStream(new FileInputStream(path));
    }

    public OutputStream openOutput(String path) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(path));
    }

    public void saveObject(Serializable object, String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)))) {
            out.writeObject(object);
        }
    }

    public Object loadObject(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(path)))) {
            return in.readObject();
        }
    }

    public String siblingPath(String path, String suffix) {
        Path p = Paths.get(path);
        String name = p.getFileName().toString();
        int dot = name.lastIndexOf('.');
        String newName = (dot > 0)
                ? name.substring(0, dot) + suffix + name.substring(dot)
                : name + suffix;
        Path parent = p.getParent();
        return (parent == null) ? newName : parent.resolve(newName).toString();
    }
}
