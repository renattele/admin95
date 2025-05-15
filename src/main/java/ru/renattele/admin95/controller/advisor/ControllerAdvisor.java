package ru.renattele.admin95.controller.advisor;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.renattele.admin95.dto.ErrorDto;
import ru.renattele.admin95.exception.ConflictException;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.exception.UnauthenticatedException;

@Slf4j
@RestControllerAdvice
@Hidden
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public Object handleResourceNotFoundException(HttpServletRequest request) {
        return handleException(request, HttpStatus.NOT_FOUND, "Not found");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public Object handleConflictException(HttpServletRequest request) {
        return handleException(request, HttpStatus.CONFLICT, "Conflict");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public Object handleUnauthenticatedException(HttpServletRequest request) {
        return handleException(request, HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Object handleBadCredentialsException(HttpServletRequest request) {
        return handleException(request, HttpStatus.BAD_REQUEST, "Bad credentials");
    }

    @ExceptionHandler(JwtException.class)
    public Object handleMalformedJwtException(HttpServletRequest request) {
        return handleException(request, HttpStatus.BAD_REQUEST, "Invalid JWT");
    }

    @ExceptionHandler(BindException.class)
    public Object handleBindException(HttpServletRequest request) {
        return handleException(request, HttpStatus.BAD_REQUEST, "Invalid request");
    }

    @ExceptionHandler(Exception.class)
    public Object handleAllOtherExceptions(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);
        return handleException(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private Object handleException(HttpServletRequest request, HttpStatus status, String message) {
        var acceptHeader = request.getHeader("Accept");
        var wantsHtml = acceptHeader != null &&
                (acceptHeader.contains("text/html") ||
                        acceptHeader.contains("application/xhtml+xml"));

        if (wantsHtml) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value());
            return new ModelAndView("forward:/error");
        }

        return ErrorDto.of(status, message);
    }
}