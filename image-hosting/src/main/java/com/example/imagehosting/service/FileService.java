package com.example.imagehosting.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    // Путь к папке для загрузки изображений (жёстко прописан)
    private final String uploadDir = "/uploads/images";

    public boolean saveFile(MultipartFile file) {
        try {
            // Путь для сохранения файла
            Path path = Paths.get(uploadDir);

            // Проверка на существование директории и её создание при необходимости
            if (!Files.exists(path)) {
                Files.createDirectories(path);  // Создание директории, если её нет
            }

            // Генерация уникального имени для файла
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = path.resolve(filename);

            // Сохранение файла
            file.transferTo(filePath);  // Запись файла в директорию

            return true;
        } catch (IOException e) {
            e.printStackTrace();  // Вывод ошибок, если не удалось сохранить файл
            return false;
        }
    }

    public boolean deleteFile(String filename) {
        try {
            // Путь к файлу для удаления
            Path filePath = Paths.get(uploadDir, filename);
            return Files.deleteIfExists(filePath);  // Удаление файла, если он существует
        } catch (IOException e) {
            e.printStackTrace();  // Вывод ошибок, если не удалось удалить файл
            return false;
        }
    }
}
