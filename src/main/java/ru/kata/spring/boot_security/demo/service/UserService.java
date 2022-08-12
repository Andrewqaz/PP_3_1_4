package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    void addUser(User user);

    boolean deleteUserById(int id);

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByEmail(String email);

    boolean updateUser(User user);

    List<Role> listRoles();

    Set<Role> getRolesFromString(String roleName);

    Role getRoleByName(String roleName);

}
