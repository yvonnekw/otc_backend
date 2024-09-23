package com.otc.backend.response;

public class ApiResponse <T> {
    private boolean success;
    private String message;
    private T data;
   // private String rabbitMQStatus;
 //String rabbitMQStatus)
 /*   public ApiResponse(boolean success, String message, T data ) {
        this.success = success;
        this.message = message;
        this.data = data;
        //this.rabbitMQStatus = rabbitMQStatus;
    }*/

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //public String getRabbitMQStatus() {
        //return rabbitMQStatus;
    //}

    //public void setRabbitMQStatus(String rabbitMQStatus) {
       // this.rabbitMQStatus = rabbitMQStatus;
    //}

    
}
