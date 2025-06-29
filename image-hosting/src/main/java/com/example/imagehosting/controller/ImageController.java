package com.example.imagehosting.controller;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class ImageController {

    private static final Logger logger = Logger.getLogger(ImageController.class.getName());

    private final ImageService imageService;
//    private final String uploadDir = "/uploads/images";
@Value("${spring.file.upload-dir}")  // Загрузка пути из application.yml

private String uploadDir;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Главная страница
    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("images", imageService.getAllItems());
        return "index";
    }

    // Обработка обновления данных изображения
//    @PostMapping("/images/update/{filename}")
//    public String updateImage(@PathVariable("filename") String filename,
//                              @RequestParam("name") String name,
//                              @RequestParam("description") String description,
//                              @RequestParam("file") MultipartFile file, Model model) {
//        Item item = imageService.getItemByFilename(filename);  // Получаем изображение по имени файла
//        if (item != null) {
//            item.setName(name);  // Обновляем имя изображения
//            item.setDescription(description);  // Обновляем описание
//
//            // Логика для обновления файла
//            if (!file.isEmpty()) {
//                String newFilename = filename;  // Можно переименовать файл при необходимости
//                Path filePath = Paths.get(uploadDir).resolve(newFilename);
//                try {
//                    Files.write(filePath, file.getBytes());  // Сохраняем новый файл
//                } catch (IOException e) {
//                    model.addAttribute("error", "Ошибка при загрузке файла");
//                    return "redirect:/";
//                }
//                item.setLink(newFilename); // Обновляем ссылку на файл в объекте
//            }
//
//            imageService.saveItem(item);  // Сохраняем обновленную запись
//            return "redirect:/";  // Перенаправляем на главную страницу после успешного обновления
//        }
//        model.addAttribute("error", "Изображение не найдено!");
//        return "redirect:/";  // Перенаправляем на главную страницу, если изображение не найдено
//    }
    @PostMapping("/images/add")
    public String addFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("description") String description,
                          @RequestParam("name") String name, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("error", "Файл не выбран!");
            return "redirect:/#gallery-headline";
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setLink(filename);

        imageService.saveItem(item);

        model.addAttribute("images", imageService.getAllItems());
        return "redirect:/#gallery-headline";
    }



    // Страница редактирования изображения
//    @GetMapping("/images/edit/{filename}")
//    public String editImage(@PathVariable("filename") String filename, Model model) {
//        // Здесь мы ищем изображение по имени файла
//        Item item = imageService.getItemByFilename(filename);
//        if (item != null) {
//            model.addAttribute("item", item);
//            return "edit-image"; // Шаблон редактирования
//        }
//        model.addAttribute("error", "Изображение не найдено!");
//        return "redirect:/"; // Перенаправляем на главную, если файл не найден
//    }
//    @GetMapping("/images/edit/{id}")
//    public String editImage(@PathVariable("id") Long id, Model model) {
//        Item item = imageService.getItemById(id);
//        if (item != null) {
//            model.addAttribute("item", item);
//            return "edit-image";
//        }
//        model.addAttribute("error", "Изображение не найдено!");
//        return "redirect:/";
//    }

    // Обработка обновления данных изображения
    @PostMapping("/images/update/{id}")
    public String updateImage(@PathVariable("id") Long id,
                              @RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("file") MultipartFile file, Model model) {
        Item item = imageService.getItemById(id);
        if (item != null) {
            item.setName(name);
            item.setDescription(description);

            if (!file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir).resolve(filename);
                try {
                    Files.write(filePath, file.getBytes());
                } catch (IOException e) {
                    model.addAttribute("error", "Ошибка при загрузке файла");
                    return "redirect:/";
                }
                item.setLink(filename);
            }

            imageService.saveItem(item);
            return "redirect:/";
        }
        model.addAttribute("error", "Изображение не найдено для редактирования!");
        return "redirect:/";
    }
//    @PostMapping("/images/update/{id}")
//    public String updateImage(@PathVariable("id") Long id,
//                              @RequestParam("name") String name,
//                              @RequestParam("description") String description,
//                              @RequestParam("file") MultipartFile file, Model model) {
//        Item item = imageService.getItemById(id);  // Получаем изображение по ID
//        if (item != null) {
//            item.setName(name);  // Обновляем имя изображения
//            item.setDescription(description);  // Обновляем описание
//
//            if (!file.isEmpty()) {  // Проверка на пустой файл
//                // Генерация уникального имени файла
//                String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//
//                // Путь к сохранению файла
//                Path filePath = Paths.get(uploadDir).resolve(filename);
//
//                // Проверяем, существует ли папка для загрузки изображений
//                Path uploadPath = Paths.get(uploadDir);
//                if (Files.notExists(uploadPath)) {
//                    try {
//                        Files.createDirectories(uploadPath);  // Создаем папку, если она не существует
//                    } catch (IOException e) {
//                        logger.severe("Не удалось создать директорию для загрузки: " + e.getMessage());
//                        model.addAttribute("error", "Ошибка при создании директории для загрузки");
//                        return "redirect:/";
//                    }
//                }
//
//                // Проверка типа файла (только изображения)
//                String fileType = file.getContentType();
//                if (fileType == null || !fileType.startsWith("image/")) {
//                    model.addAttribute("error", "Неверный формат файла. Пожалуйста, выберите изображение.");
//                    return "redirect:/";
//                }
//
//                // Проверка размера файла
//                if (file.getSize() > 10 * 1024 * 1024) {  // Ограничение размера файла 10MB
//                    model.addAttribute("error", "Размер файла слишком большой. Максимальный размер 10MB.");
//                    return "redirect:/";
//                }
//
//                // Сохранение файла на диск
//                try {
//                    Files.write(filePath, file.getBytes());  // Запись файла
//                } catch (IOException e) {
//                    logger.severe("Ошибка при загрузке файла: " + e.getMessage());
//                    model.addAttribute("error", "Ошибка при загрузке файла");
//                    return "redirect:/";
//                }
//
//                // Обновляем ссылку на файл в объекте Item
//                item.setLink(filename);
//            }
//
//            // Сохраняем обновленные данные изображения
//            imageService.saveItem(item);
//            return "redirect:/";  // Перенаправляем на главную страницу после успешного обновления
//        }
//        model.addAttribute("error", "Изображение не найдено для редактирования!");  // Сообщение об ошибке, если изображение не найдено
//        return "redirect:/";  // Перенаправляем на главную страницу
//    }


    // Удаление изображения
    @PostMapping("/images/delete/{id}")
    public String deleteImage(@PathVariable("id") Long id, Model model) {
        Item item = imageService.getItemById(id);
        if (item != null) {
            imageService.deleteItem(id);

            // Удаляем файл с диска
            File fileToDelete = new File(uploadDir + "/" + item.getLink());
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }
        return "redirect:/";
    }
}
