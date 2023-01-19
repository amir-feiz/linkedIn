package com.example.linkedin.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("this password is invalid!");
    }
}
