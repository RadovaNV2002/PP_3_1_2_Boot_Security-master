package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUser(Long userId) throws NullPointerException {
        return userRepository.findByID(userId);
    }

    @Transactional
    @Override
    public boolean saveUser(User user, BindingResult bindingResult, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void tryIndex(Model model, HttpSession session, LoginException authenticationException, String authenticationName) {

        if (authenticationException != null) { // Восстанавливаем неверно введенные данные
            try {
                model.addAttribute("authenticationException", authenticationException);
                session.removeAttribute("Authentication-Exception");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            model.addAttribute("authenticationException", new LoginException(null));
        }

        if (authenticationName != null) { // Выводим прощальное сообщение
            try {
                model.addAttribute("authenticationName", authenticationName);
                session.removeAttribute("Authentication-Name");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByMail(email);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("User email %s not found", email));
        }
        return user;
    }



    @Override
    public List<Role> getAllRoles() {
        return roleRepository.listAllRoles();
    }

    @Override
    public Set<Role> getRoleSet(Set<String> roles) {
        return roleRepository.getRoleSet(roles);
    }

    @Override
    public Role getDefaultRole() {
        return roleRepository.getDefaultRole();
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.getAdminRole();
    }

    @Override
    public void setRolesDefault() {
        roleRepository.setAdminRoleDefault();
        roleRepository.setUserRoleDefault();
    }
}