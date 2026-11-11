package main.java;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagFrequency {
    private static final Pattern OPEN_TAG = Pattern.compile("<\\s*([a-zA-Z][a-zA-Z0-9-]*)");

    public TreeMap<String, Integer> count(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Сервер повернув код " + response.statusCode());
        }
        return countInHtml(response.body());
    }

    public TreeMap<String, Integer> countInHtml(String html) {
        TreeMap<String, Integer> result = new TreeMap<>();
        Matcher m = OPEN_TAG.matcher(html);
        while (m.find()) {
            result.merge(m.group(1).toLowerCase(), 1, Integer::sum);
        }
        return result;
    }


    public List<Map.Entry<String, Integer>> sortedByName(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return list;
    }

    public List<Map.Entry<String, Integer>> sortedByFrequency(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()));
        return list;
    }
}