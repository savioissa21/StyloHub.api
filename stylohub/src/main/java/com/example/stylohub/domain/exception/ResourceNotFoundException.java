package com.example.stylohub.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " não encontrado(a) com identificador: " + identifier);
    }
}
