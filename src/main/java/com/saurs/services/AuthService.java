package com.saurs.services;

import com.saurs.enums.RoleEnum;
import com.saurs.exceptions.BadCredentialsException;
import com.saurs.exceptions.UserAlreadyExistsException;
import com.saurs.models.*;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtService tokenService;

    @Inject
    public AuthService(UserService userService, RoleService roleService, JwtService tokenService) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    public TokenResponse login(Auth auth) {
        User user = userService.findByEmail(auth.email());
        if (!BcryptUtil.matches(auth.password(), user.password)) {
            throw new BadCredentialsException();
        }
        return tokenService.generateTokens(user);
    }

    public TokenResponse register(Register userRegister) {
        Role role = roleService.findByName(RoleEnum.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User(userRegister.firstName(), userRegister.lastName(), userRegister.email(), userRegister.password(), roles);
        user.email = userRegister.email();
        user.password = userRegister.password();
        User savedUser = userService.save(user);
        return tokenService.generateTokens(savedUser);
    }

    public TokenResponse refresh(RefreshToken token) {
        return tokenService.refreshTokens(token);
    }

    public User getAuthenticatedUser(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BadCredentialsException();
        }
        String token = authorization.substring(7);
        return tokenService.validateToken(token);
    }
}
