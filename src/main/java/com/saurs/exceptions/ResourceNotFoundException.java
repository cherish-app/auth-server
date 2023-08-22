package com.saurs.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String identifier) {
        super("Resource with identifier " + identifier + " not found.");
    }
}
