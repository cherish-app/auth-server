package com.saurs.services;

import com.saurs.exceptions.ResourceNotFoundException;
import com.saurs.exceptions.UserAlreadyExistsException;
import com.saurs.models.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class UserService {

    public User findByEmail(String email) {
        User foundUser = User.find("email", email).firstResult();
        if (foundUser == null) {
            throw new ResourceNotFoundException(email);
        }
        return foundUser;
    }

    public User findById(UUID id) {
        User foundUser = User.findById(id);
        if (foundUser == null) {
            throw new ResourceNotFoundException(id.toString());
        }
        return foundUser;
    }

    @Transactional
    public User save(User user) {
        if (User.count("email", user.email) > 0) {
            throw new UserAlreadyExistsException(user.email);
        }
        user.password = BcryptUtil.bcryptHash(user.password);
        return User.save(user);
    }

    @Transactional
    public User update(User user, UUID id) {
        User userToUpdate = findById(id);
        userToUpdate.firstName = user.firstName != null ? user.firstName : userToUpdate.firstName;
        userToUpdate.lastName = user.lastName != null ? user.lastName : userToUpdate.lastName;
        userToUpdate.email = user.email != null ? user.email : userToUpdate.email;
        User.save(userToUpdate);
        return user;
    }

    @Transactional
    public void delete(UUID id) {
        User user = findById(id);
        User.delete("id", user.id);
    }
}
