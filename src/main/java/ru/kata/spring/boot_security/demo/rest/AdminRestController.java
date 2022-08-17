package ru.kata.spring.boot_security.demo.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class AdminRestController {
    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    public AdminRestController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = service.getAllUsers();
        return allUsers != null
                ? new ResponseEntity<>(allUsers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = service.getUserById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        service.addUser(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/admin")
    public ResponseEntity<?> update(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        int i = 1;
        return service.updateUser(user)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        return service.deleteUserById(id)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "admin/roles")
    public ResponseEntity<?> getRoles() {
        List<Role> roles = service.listRoles();
        return roles != null
                ? new ResponseEntity<>(roles, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
