package com.example.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FrontendController {

    @GetMapping({"/", "/index"})
    public String index() {
        // Галерея подгружается на клиенте через JS fetch('/api/images')
        return "index";
    }

    @GetMapping("/edit-item")
    public String editItem() {
        return "edit-item";
    }

    @GetMapping("/email-page")
    public String emailPage() {
        return "email-page";
    }

//    @GetMapping({"/", "/index"})
//    public String index(@RequestParam(value = "message", required = false) String message, Model model) {
//        if (message != null) model.addAttribute("message", message);
//        // НЕ делаем REST-запрос к /api/images!
//        // Галерея загружается через JS или отдельный фрагмент
//        return "index";
//    }
//
//    @GetMapping("/edit-item")
//    public String editItem(@RequestParam("id") Long id, Model model) {
//        model.addAttribute("id", id);
//        return "edit-item";
//    }
//    @GetMapping("/email-page")
//    public String emailPage(@RequestParam(value = "message", required = false) String message, Model model) {
//        if (message != null) model.addAttribute("message", message);
//        return "email-page";
//    }

    @GetMapping("/oops")
    public String oops() {
        return "oops";
    }
}
