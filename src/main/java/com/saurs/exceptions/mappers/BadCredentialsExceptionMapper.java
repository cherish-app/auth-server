package com.saurs.exceptions.mappers;

import com.saurs.exceptions.BadCredentialsException;
import com.saurs.exceptions.models.StandardError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class BadCredentialsExceptionMapper implements ExceptionMapper<BadCredentialsException> {

    @Override
    public Response toResponse(BadCredentialsException exception) {
        Response.StatusType status = Response.Status.UNAUTHORIZED;
        return Response.status(status)
                .entity(new StandardError(
                        LocalDateTime.now(),
                        status.getStatusCode(),
                        status.getReasonPhrase(),
                        exception.getMessage()
                ))
                .build();
    }
}
