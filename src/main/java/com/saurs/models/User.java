package com.saurs.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@UserDefinition
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    public UUID id;
    public String firstName;
    public String lastName;
    @Email
    @Username
    public String email;
    @Password
    public String password;
    @ManyToMany
    @Roles
    public Set<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, @Email String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public static User save(User user) {
        persist(user);
        flush();
        return user;
    }
}
