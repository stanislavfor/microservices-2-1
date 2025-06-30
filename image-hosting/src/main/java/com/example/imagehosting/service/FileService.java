package com.example.imagehosting.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String uploadDir = "./uploads/images";

    // Сохранение файла с уникальным именем. Возвращает имя для ссылки
    public String saveFile(MultipartFile file) {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // Генерация уникального имени файла
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = path.resolve(filename);
            file.transferTo(filePath);
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*/ Сохранение файла под заданным именем, например, для обновления
     *
     */
    public boolean saveFile(MultipartFile file, String filename) {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path filePath = path.resolve(filename);
            file.transferTo(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Удаление файла по имени
    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
