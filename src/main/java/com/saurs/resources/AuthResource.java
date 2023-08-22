package com.saurs.resources;

import com.saurs.models.*;
import com.saurs.services.AuthService;
import io.vertx.mutiny.core.http.HttpServerRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;


import java.net.http.HttpRequest;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
public class AuthResource {

    private static final Logger LOGGER = Logger.getLogger(AuthResource.class.getName());

    private final AuthService authService;

    @Inject
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @Produces(APPLICATION_JSON)
    public TokenResponse login(@RequestBody @Valid Auth auth) {
        return authService.login(auth);
    }

    @POST
    @PermitAll
    @Path("/register")
    @Produces(APPLICATION_JSON)
    public TokenResponse register(@RequestBody @Valid Register userRegister) {
        return authService.register(userRegister);
    }

    @POST
    @PermitAll
    @Path("/refresh")
    @Produces(APPLICATION_JSON)
    public TokenResponse refresh(@RequestBody @Valid RefreshToken token) {
        return authService.refresh(token);
    }

    @GET
    @PermitAll
    @Path("/whoami")
    @Produces(APPLICATION_JSON)
    public User getAuthenticatedUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
        return authService.getAuthenticatedUser(token);
    }
}
