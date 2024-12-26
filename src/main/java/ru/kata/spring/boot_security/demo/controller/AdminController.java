package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAllUsers());

        return "user-list";
    }

    @GetMapping(value = "/new")
    public String addUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", userService.findAllRoles());

        return "new";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable(value = "id", required = true) Long userId, Model model) {
        try {
            model.addAttribute("user", userService.findUser(userId));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();

            return "redirect:/admin";
        }
        model.addAttribute("allRoles", userService.findAllRoles());

        return "new";
    }

    @PostMapping()
    public String saveOrUpdateUser(@ModelAttribute("user") User user,
                                   BindingResult bindingResult, Model model) {
        // Непонятно как избавиться от этого
        // Поймать в сервисе транзакционный эксепшн нельзя
        try {
            return userService.saveUser(user, bindingResult, model) ? "redirect:/admin" : "user-form";
        } catch (Exception e) {
            return "new";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);

        return "redirect:/admin";
    }
}