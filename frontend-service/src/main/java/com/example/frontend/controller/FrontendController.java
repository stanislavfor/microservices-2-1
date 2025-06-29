package com.example.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/index")
    public String index(Model model) {
        // Можно добавить атрибуты в модель, если нужно передать данные в шаблон
        model.addAttribute("message", "Welcome to the Image Hosting!");
        return "index";  // Возвращает index.html из папки templates
    }
}
