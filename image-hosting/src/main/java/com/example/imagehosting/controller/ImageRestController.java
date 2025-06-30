package com.example.imagehosting.controller;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.service.FileService;
import com.example.imagehosting.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

    private final ImageService imageService;
    private final FileService fileService;

    public ImageRestController(ImageService imageService, FileService fileService) {
        this.imageService = imageService;
        this.fileService = fileService;
    }

    // Получить все изображения (JSON)
    @GetMapping
    public ResponseEntity<List<Item>> getAllImages() {
        List<Item> images = imageService.findAll();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();  // Возвращаем 204, если изображений нет
        }
        return ResponseEntity.ok(images);
    }

    // Получить изображение по ID (JSON)
    @GetMapping("/{id}")
    public ResponseEntity<Item> getImageById(@PathVariable Long id) {
        Item item = imageService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();  // Возвращаем 404, если изображение не найдено
        }
        return ResponseEntity.ok(item);
    }

    // Загрузка файла через multipart/form-data (от формы)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Item> uploadImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) {
        // Проверка на пустой файл
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();  // Возвращаем 400, если файл не был выбран
        }

        // Сохранение файла
        String filename = fileService.saveFile(file);
        if (filename == null) {
            return ResponseEntity.internalServerError().build();  // Возвращаем 500, если произошла ошибка при сохранении
        }

        // Создание и сохранение Item
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setLink(filename);
        Item saved = imageService.saveItem(item);

        return ResponseEntity.ok(saved);  // Возвращаем 200 с сохраненным объектом
    }

    // Добавить Item через JSON (на будущее, если нужно)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> saveImageJson(@RequestBody Item item) {
        if (item == null) {
            return ResponseEntity.badRequest().build();  // Проверка на null объекта
        }
        Item saved = imageService.saveItem(item);
        return ResponseEntity.ok(saved);  // Возвращаем сохраненный объект
    }

    // Удалить изображение
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        Item item = imageService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();  // Если элемент не найден, возвращаем 404
        }
        imageService.deleteImage(id);  // Удаляем изображение
        return ResponseEntity.noContent().build();  // Возвращаем 204 после успешного удаления
    }
}
