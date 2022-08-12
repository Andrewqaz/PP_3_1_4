package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping(value = "/")
public class LoginController {

    @GetMapping
    public String goToTargetPage(@AuthenticationPrincipal User authorizedUser) {
        if (authorizedUser == null) {
            return "redirect:/login";
        }
        if ((authorizedUser.getRoles().stream()
                .filter(role -> role.getName().equals("ADMIN")).count() > 0)) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

}
