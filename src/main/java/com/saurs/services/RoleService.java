package com.saurs.services;

import com.saurs.enums.RoleEnum;
import com.saurs.exceptions.ResourceNotFoundException;
import com.saurs.models.Role;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleService {

    public Role findByName(RoleEnum role) {
        Role foundRole = Role.find("name", role).firstResult();
        if (foundRole == null) {
            throw new ResourceNotFoundException(role.name());
        }

        return foundRole;
    }
}
