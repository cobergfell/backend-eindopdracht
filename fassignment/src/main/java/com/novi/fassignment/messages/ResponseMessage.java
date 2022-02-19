//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.messages;


public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}