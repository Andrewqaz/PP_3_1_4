package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        Set<Role> roles = new HashSet<>();
        for (Role r : user.getRoles()) {
            roles.add(roleDao.getByName(r.getName()));
        }
        user.setRoles(roles);
        userDao.addUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public boolean deleteUserById(int id) {
        return userDao.deleteUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String login) {
        return userDao.getUserByEmail(login);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        if (user.getPassword() == null || user.getPassword().equals("")) {
            user.setPassword(userDao.getUserById(user.getId()).getPassword());
        }
        Set<Role> roles = new HashSet<>();
        for (Role r : user.getRoles()) {
            roles.add(roleDao.getByName(r.getName()));
        }
        user.setRoles(roles);
        return userDao.updateUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> listRoles() {
        return roleDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getRolesFromString(String stringRole) {
        Set<Role> resultRoleSet = new HashSet<>();
        String[] rolesNames = stringRole.split(" ");
        for (String rName : rolesNames) {
            resultRoleSet.add(roleDao.getByName(rName));
        }
        return resultRoleSet;
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        return roleDao.getByName(roleName);
    }
}
