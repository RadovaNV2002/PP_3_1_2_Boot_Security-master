package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleRepository {

    List<Role> findAll();

    Set<Role> getRoleSet(Set<String> role);

    Role getDefaultRole();

    Role getRoleByName(String name);

    Role getAdminRole();

    void setAdminRoleDefault();

    void setUserRoleDefault();

    List<Role> listAllRoles();
}