package com.otc.backend.exception;

public class DatabaseException extends RuntimeException{

    private static final long serialVersionUID = 1l;
    
    public DatabaseException(String string, Exception ex) {
        super("The user you are looking for does not exist");
    }

}
