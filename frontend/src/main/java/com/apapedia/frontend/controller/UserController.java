package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apapedia.frontend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("token");
        
        var user = userService.getUserById(id, token);
        model.addAttribute("user", user);
        return "user/profile";
    }
}
