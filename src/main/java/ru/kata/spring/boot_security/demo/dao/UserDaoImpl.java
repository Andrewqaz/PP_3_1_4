package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public boolean deleteUserById(int id) {
        if (getUserById(id) == null) {
            return false;
        }
        entityManager.remove(entityManager.find(User.class, id));
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User").getResultList();
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByEmail(String email) {
        Query query = entityManager.createQuery("from User where email = :email");
        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    @Override
    public boolean updateUser(User user) {
        User merge = entityManager.merge(user);
        return merge != null;
    }
}
