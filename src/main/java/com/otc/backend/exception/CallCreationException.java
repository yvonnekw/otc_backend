package com.otc.backend.exception;

public class CallCreationException extends RuntimeException {


    private static final long serialVersionUID = 1l;

    public CallCreationException(String s) {
        super("The call details you are looking for does not exist");
    }

}
