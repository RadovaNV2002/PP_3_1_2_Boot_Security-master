package ru.kata.spring.boot_security.demo.model;


import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "role", unique = true, nullable = false)
    String name;

    @ManyToMany(mappedBy = "roleSet")
    private Set<User> userSet = new HashSet<>();

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getRole_id() {
        return id;
    }

    public void setRole_id(Long role_id) {
        this.id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(getName(), role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }
}
