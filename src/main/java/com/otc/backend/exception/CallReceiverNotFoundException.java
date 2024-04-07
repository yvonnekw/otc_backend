package com.otc.backend.exception;

public class CallReceiverNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    public CallReceiverNotFoundException() {
        super("The call reciever you are looking for does not exist");
    }

}
