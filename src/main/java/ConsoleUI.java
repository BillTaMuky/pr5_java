package main.java;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final FileService files = new FileService();
    private final LongestLineFinder finder = new LongestLineFinder();
    private final CaesarCipher cipher = new CaesarCipher(files);
    private final TagFrequency tags = new TagFrequency();

    public void run() {
        while (true) {
            printMenu();
            String choice;
            try {
                choice = ask("Ваш вибір: ");
            } catch (NoSuchElementException e) {
                return;
            }
            try {
                switch (choice) {
                    case "1" -> task1();
                    case "2" -> encrypt();
                    case "3" -> decrypt();
                    case "4" -> task4();
                    case "5" -> loadTagResults();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Невідомий пункт меню.");
                }
            } catch (IOException e) {
                System.out.println("Помилка файлу/мережі: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректне введення: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Файл містить невідомий тип даних.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Операцію перервано.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nМеню");
        System.out.println("1 - Рядок з максимальною кількістю слів");
        System.out.println("2 - Зашифрувати файл");
        System.out.println("3 - Розшифрувати файл");
        System.out.println("4 - Частота тегів за URL");
        System.out.println("5 - Завантажити збережені результати частоти тегів");
        System.out.println("0 - Вихід");
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String askNonEmpty(String prompt) {
        String s = ask(prompt);
        if (s.isEmpty()) {
            throw new IllegalArgumentException("значення не може бути порожнім");
        }
        return s;
    }

    private char askKey() {
        String s = askNonEmpty("Ключовий символ: ");
        return s.charAt(0);
    }

    private void task1() throws IOException {
        String path = askNonEmpty("Шлях до файлу: ");
        List<String> lines = files.readLines(path);
        Optional<String> best = finder.find(lines);
        if (best.isEmpty()) {
            System.out.println("Файл порожній.");
        } else {
            System.out.println("Слів: " + LongestLineFinder.countWords(best.get()));
            System.out.println("Рядок: " + best.get());
        }
    }

    private void encrypt() throws IOException {
        String path = askNonEmpty("Шлях до файлу для шифрування: ");
        char key = askKey();
        String out = files.siblingPath(path, "_encrypted");
        cipher.encrypt(path, out, key);
        System.out.println("Зашифровано: " + out);
    }

    private void decrypt() throws IOException {
        String path = askNonEmpty("Шлях до зашифрованого файлу: ");
        char key = askKey();
        String out = files.siblingPath(path, "_decrypted");
        cipher.decrypt(path, out, key);
        System.out.println("Розшифровано: " + out);
    }

    private void task4() throws IOException, InterruptedException {
        String url = askNonEmpty("URL сторінки (http/https): ");
        URI uri = URI.create(url);
        if (uri.getScheme() == null || !(uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
            throw new IllegalArgumentException("URL має починатися з http:// або https://");
        }
        TreeMap<String, Integer> result = tags.count(url);
        printTags(result);

        String save = ask("Зберегти результат у файл? (так/ні): ");
        if (save.equalsIgnoreCase("так") || save.equalsIgnoreCase("y")) {
            String path = askNonEmpty("Шлях і ім'я файлу для збереження: ");
            files.saveObject(result, path);
            System.out.println("Збережено: " + path);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTagResults() throws IOException, ClassNotFoundException {
        String path = askNonEmpty("Шлях до файлу з результатами: ");
        Object obj = files.loadObject(path);
        if (!(obj instanceof Map)) {
            throw new IllegalArgumentException("у файлі не результати підрахунку тегів");
        }
        printTags((Map<String, Integer>) obj);
    }

    private void printTags(Map<String, Integer> map) {
        System.out.println("\n За алфавітом (зростання)");
        for (Map.Entry<String, Integer> e : tags.sortedByName(map)) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        System.out.println("\n За частотою (зростання)");
        for (Map.Entry<String, Integer> e : tags.sortedByFrequency(map)) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }
}