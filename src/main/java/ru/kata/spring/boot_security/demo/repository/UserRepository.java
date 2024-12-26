package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    User findByID(Long id);

    User findByMail(String email);

    void save(User entity);

    void update(Long id, User updatedUser);

    void delete(Long id);
}
