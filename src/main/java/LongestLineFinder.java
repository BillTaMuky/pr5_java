package main.java;

import java.util.List;
import java.util.Optional;

public class LongestLineFinder {

    public static int countWords(String line) {
        String trimmed = line.trim();
        return trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
    }

    public Optional<String> find(List<String> lines) {
        String best = null;
        int bestCount = -1;
        for (String line : lines) {
            int count = countWords(line);
            if (count > bestCount) {
                bestCount = count;
                best = line;
            }
        }
        return Optional.ofNullable(best);
    }

}
