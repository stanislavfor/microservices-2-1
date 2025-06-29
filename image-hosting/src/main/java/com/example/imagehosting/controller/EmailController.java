package com.example.imagehosting.controller;

import com.example.imagehosting.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email-page")
    public String showForm() {
        return "email-page";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("username") String username,
                            @RequestParam("email") String email,
                            @RequestParam("message") String message,
                            Model model) {
        try {
            emailService.saveEmail(username, email, message);
            model.addAttribute("message", "Сообщение email отправлено!");
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка отправки сообщения email.");
            e.printStackTrace();
        }
        return "email-page";
    }
}


//package com.example.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Controller
//public class EmailController {
//
//    @GetMapping("/images/email-page")
//    public String showForm() {
//        return "email-page";
//    }
//
//    @PostMapping("/images/sendEmail")
//    public String sendEmail(@RequestParam("username") String username,
//                            @RequestParam("email") String email,
//                            @RequestParam("message") String message,
//                            Model model) {
//        String filePath = "uploads/messages.txt";
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = now.format(formatter);
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
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
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        model.addAttribute("message", "Email sent successfully!");
//        return "email-page.html";
//    }
//}