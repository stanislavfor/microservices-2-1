package com.example.frontend.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class PageController {
    @GetMapping("/index")
    public String index() {
        return "index"; // Возвращает шаблон index.html
    }

    @GetMapping("/email-page")
    public String emailPage() {
        return "email-page"; // Возвращает шаблон email-page.html
    }

    @GetMapping("/oops")
    public String oops() {
        return "oops"; // Возвращает шаблон oops.html
    }
}
