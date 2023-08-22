package com.saurs.exceptions.mappers;

import com.saurs.exceptions.BadCredentialsException;
import com.saurs.exceptions.ResourceNotFoundException;
import com.saurs.exceptions.models.StandardError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        Response.StatusType status = Response.Status.NOT_FOUND;
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
