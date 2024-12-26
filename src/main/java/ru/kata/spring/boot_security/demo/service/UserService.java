package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> findAllUsers();

    User findUser(Long userId) throws NullPointerException;

    void deleteUser(Long id);

    List<Role> findAllRoles();

    void tryIndex(Model model, HttpSession session, LoginException authenticationException, String authenticationName);

    boolean saveUser(User user, BindingResult bindingResult, Model model);

    List<Role> getAllRoles();

    Set<Role> getRoleSet(Set<String> roles);

    Role getDefaultRole();

    Role getAdminRole();

    void setRolesDefault();
}
