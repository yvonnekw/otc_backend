package com.otc.backend.exception;

public class CallReceiverCreationException extends RuntimeException {


    private static final long serialVersionUID = 1l;

   // public CallReceiverCreationException() {
       // super("New call receiver details were not successfully registered");
    //}

    public CallReceiverCreationException() {
        super("New call receiver details were not successfully registered");
    }

    public CallReceiverCreationException(String message) {
        super(message);
    }

    public CallReceiverCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallReceiverCreationException(Throwable cause) {
        super(cause);
    }

}
