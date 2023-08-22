package com.saurs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saurs.enums.RoleEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.RolesValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Role extends PanacheEntity {

    @RolesValue
    @Enumerated(EnumType.STRING)
    public RoleEnum name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    public List<User> users;

    public Role(RoleEnum roleEnum) {
        this.name = roleEnum;
    }

    public Role() {
    }

    public static Role save(Role role) {
        persist(role);
        flush();
        return role;
    }
}
