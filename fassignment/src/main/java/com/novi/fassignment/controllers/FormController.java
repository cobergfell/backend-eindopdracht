package com.novi.fassignment.controllers;

import com.novi.fassignment.models.FormSender;
import com.novi.fassignment.services.SendingMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class FormController {
    @Autowired
    SendingMailService sendingMailService;

    @GetMapping("/")
    public String index() {
        return "redirect:/form";
    }

    @GetMapping("/form")
    public String formGet(Model model) {
        model.addAttribute("user", new FormSender());
        return "form";
    }

    @PostMapping("/form")
    public String formPost(@Valid FormSender formSender, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        model.addAttribute("noErrors", true);
        model.addAttribute("user", formSender);
        String subject = formSender.getName() + " " + formSender.getEmail() + " sent you a message";
        sendingMailService.sendMail(subject, formSender.getMessage());
        return "form";
    }
}
