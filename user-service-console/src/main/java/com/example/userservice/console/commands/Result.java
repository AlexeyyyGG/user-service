package com.example.userservice.console.commands;

public class Result {
    private final String successMessage;
    private final String errorMessage;

    private Result(String successMessage, String errorMessage) {
        this.successMessage = successMessage;
        this.errorMessage = errorMessage;
    }

    public static Result success(String message) {
        return new Result(message, null);
    }

    public static Result failure(String errorMessage) {
        return new Result(null, errorMessage);
    }

    public boolean isSuccess() {
        return successMessage != null;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}