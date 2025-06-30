package com.example.imagehosting.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final String FILE_PATH = "./uploads/messages.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MAX_WORDS_PER_LINE = 11; // Количество слов в одной строке, для читаемости кода

    public void saveEmail(String username, String email, String message) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(FORMATTER);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("Date: " + formattedDate);
            writer.newLine();
            writer.write("Username: " + username);
            writer.newLine();
            writer.write("Email: " + email);
            writer.newLine();
            writer.write("Message: ");
            writer.newLine();

            // Форматирование сообщения
            String formattedMessage = formatMessage(message);
            writer.write(formattedMessage);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
        }
    }

    private String formatMessage(String message) {
        String[] words = message.split(" ");
        StringBuilder formattedMessage = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() + 1 > MAX_WORDS_PER_LINE * 10) { // 10 - примерная длина одного слова с пробелом
                formattedMessage.append(line.toString().trim());
                formattedMessage.append(System.lineSeparator());
                line.setLength(0);
            }
            line.append(word).append(" ");
        }

        // Добавление последней строки
        formattedMessage.append(line.toString().trim());
        return formattedMessage.toString();
    }



}



//package com.example.service;
//
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class EmailService {
//
//    private static final String FILE_PATH = "uploads/messages.txt";
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    public void saveEmail(String username, String email, String message) throws IOException {
//        LocalDateTime now = LocalDateTime.now();
//        String formattedDate = now.format(FORMATTER);
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
//            writer.write("Date: " + formattedDate);
//            writer.newLine();
//            writer.write("Username: " + username);
//            writer.newLine();
//            writer.write("Email: " + email);
//            writer.newLine();
//            writer.write("Message: " + message);
//            writer.newLine();
//            writer.write("--------------------------------------------------");
//            writer.newLine();
//        }
//    }
//}
