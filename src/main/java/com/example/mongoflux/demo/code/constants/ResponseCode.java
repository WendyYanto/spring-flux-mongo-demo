package com.example.mongoflux.demo.code.constants;

public enum ResponseCode {

    SUCCESS_CODE(200, "Success"),
    VALIDATION_ERROR_CODE(412, "Precondition Failed"),
    SERVER_ERROR_CODE(500, "Internal Server Error");

    private final String message;
    private final int code;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}