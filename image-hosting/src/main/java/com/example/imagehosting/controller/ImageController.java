package com.example.imagehosting.controller;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.service.FileService;
import com.example.imagehosting.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    private final FileService fileService;
    private final ImageService imageService;

    @Autowired
    public ImageController(FileService fileService, ImageService imageService) {
        this.fileService = fileService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("msg", "Image-hosting сервис работает!");
        return "hello"; // hello.html для проверки на порту 8084
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("file") MultipartFile file,
                              Model model) {
        if (file == null || file.isEmpty()) {
            model.addAttribute("message", "Файл не выбран!");
            return "redirect:/?message=Файл не выбран!";
        }

        String filename = fileService.saveFile(file);
        if (filename == null) {
            model.addAttribute("message", "Ошибка при сохранении файла!");
            return "redirect:/?message=Ошибка загрузки файла!";
        }

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setLink(filename);
        imageService.saveItem(item);

        return "redirect:/index"; // или на страницу галереи
    }
}
