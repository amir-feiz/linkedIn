package com.example.linkedin.exception;

public class UsernameExistException extends RuntimeException {
    public UsernameExistException(Long username){
        super("the "+username+" username already exist!");
    }
}