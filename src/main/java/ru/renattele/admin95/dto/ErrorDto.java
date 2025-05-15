package ru.renattele.admin95.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorDto(int code, String message) {
    public static ResponseEntity<ErrorDto> of(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorDto(status.value(), message), status);
    }
}