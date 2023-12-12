package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apapedia.frontend.dto.user.UserMapper;
import com.apapedia.frontend.dto.user.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;
import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/profile/{id}")
    public String profileView(@PathVariable String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");
        
        var user = userService.getUserById(id, token);
        model.addAttribute("activeUserId", id);
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String profileEdit(@PathVariable String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");

        var user = userService.getUserById(id, token);
        model.addAttribute("activeUserId", id);
        model.addAttribute("user", user);
        return "/user/profile-edit-form";
    }

    @PostMapping("/profile/edit/{id}")
    public String profileEditSubmit(@PathVariable String id, Model model, HttpServletRequest request, UpdateUserRequestDTO userDTO) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");
        var userEdited = userService.editUserSeller(id, token, userDTO);
        model.addAttribute("activeUserId", id);
        model.addAttribute("user", userEdited);
        return "redirect:/user/profile/{id}";
    }

    @GetMapping("/profile/withdraw/{id}")
    public String profileWithdraw(@PathVariable String id, Model model) {
        model.addAttribute("activeUserId", id);
        return "/user/withdraw-form";
    }

    @PutMapping("/profile/withdraw/{id}")
    public String profileWithdrawSubmit(@PathVariable String id, Model model, HttpServletRequest request, @RequestParam int balance) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");
        var response = userService.subtractBalance(id, token, balance);
        model.addAttribute("activeUserId", id);
        model.addAttribute("response", response);
        return "/user/withdraw-success";
    }

    @GetMapping("/profile/delete/{id}")
    public ModelAndView profileDelete(@PathVariable String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        var token = (String) session.getAttribute("accessToken");
        userService.deleteUser(id, token);

        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }


}
