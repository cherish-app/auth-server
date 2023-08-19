package com.saurs.services;

import com.saurs.models.User;
import com.saurs.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.find("email", email).firstResult();
    }
}
