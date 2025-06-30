package com.example.imagehosting.controller;

import com.example.imagehosting.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // GET /email-page Возвращает страницу с формой отправки сообщения. Проксируется через gateway как /email-page.
    @GetMapping("/email-page")
    public String showForm(Model model, @RequestParam(value = "message", required = false) String message) {
        // Показываем flash-сообщение, если есть
        model.addAttribute("message", message);
        return "email-page"; // Thymeleaf-шаблон email-page.html должен быть в frontend-service/templates
    }

    // POST /email Обрабатывает отправку сообщения с формы. Gateway проксирует POST /api/email -> /email (c StripPrefix=1).
    @PostMapping("/email")
    public String sendEmail(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model
    ) {
        // Примитивная валидация (можно заменить на @Valid и отдельный DTO)
        if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                message == null || message.trim().isEmpty()) {
            model.addAttribute("message", "Все поля обязательны для заполнения!");
            return "email-page"; // Вернем на форму с ошибкой
        }
        try {
            emailService.saveEmail(username, email, message);
            // После успешной отправки — редиректим с flash message
            return "redirect:/email-page?message=Сообщение успешно отправлено!";
        } catch (IOException e) {
            // Логируем ошибку, можно отправить на страницу oops или снова на форму
            e.printStackTrace();
            return "redirect:/oops";
        }
    }
}
