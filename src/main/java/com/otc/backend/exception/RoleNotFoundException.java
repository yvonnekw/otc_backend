package com.otc.backend.exception;

public class RoleNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1l;

    public RoleNotFoundException(String roleNotFound) {
        super("Role not found");
    }

}
