package ru.renattele.admin95.exception;

public class ResourceNotFoundException extends RuntimeException {
    // ...existing code...
    public ResourceNotFoundException() {
        super();
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
