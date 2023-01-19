package com.example.linkedin.exception;

import lombok.NoArgsConstructor;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Object id){
        super( id + " not found");
    }
    public EntityNotFoundException(){
        super("users not found");
    }
}

