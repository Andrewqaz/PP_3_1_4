package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService service;

    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String getAdminPage(Model model, @AuthenticationPrincipal User authorizedUser) {
        List<User> allUsers = service.getAllUsers();
        model.addAttribute("users", allUsers);
        model.addAttribute("authorizedUser", service.getUserByEmail(authorizedUser.getEmail()));
        model.addAttribute("listRoles", service.listRoles());
        model.addAttribute("newUser", new User());
        return "adminPage";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("newUser") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        service.deleteUserById(userId);

        return "redirect:/admin";
    }

    @PatchMapping("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                             @RequestParam(value = "nameRoles", required = false) String roles) {
        user.setRoles(service.getRolesFromString(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.updateUser(user);

        return "redirect:/admin";
    }
}
