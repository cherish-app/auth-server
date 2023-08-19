package com.saurs.resources;

import com.saurs.enums.Role;
import com.saurs.models.TokenResponse;
import com.saurs.models.User;
import com.saurs.services.JwtService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
public class AuthResource {

    @Inject
    JwtService jwtService;

    @GET
    @Path("/login")
    @Produces(APPLICATION_JSON)
    public TokenResponse login() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@mail.com");
        user.setPassword("123456");
        user.getRoles().add(Role.USER);

        return jwtService.generateTokens(user);
    }
}
