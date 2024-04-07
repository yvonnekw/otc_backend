package com.otc.backend.exception;

public class UserDoesNotExistException extends RuntimeException{

    private static final long serialVersionUID = 1l;
    
    public UserDoesNotExistException() {
        super("The user you are looking for does not exist");
    }

}
