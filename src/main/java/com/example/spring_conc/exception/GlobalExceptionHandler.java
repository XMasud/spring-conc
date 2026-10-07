package com.example.spring_conc.exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleProductNotFoundException(NotFoundException ex, HttpServletRequest request) {

        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex, HttpServletRequest request) {

        logger.log(Level.WARNING, "Unexpected error while processing request: " + ex);

        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ProblemDetail handleInvalidStatusException(InvalidStatusException ex, HttpServletRequest request) {

        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(OutOfStockException.class)
    public ProblemDetail handleOutOfStockException(
            OutOfStockException exception,
            HttpServletRequest request) {

        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Insufficient stock.",
                request
        );
    }

    @ExceptionHandler({
            OptimisticLockException.class,
            ObjectOptimisticLockingFailureException.class
    })
    public ProblemDetail handleOptimisticLockException(
            Exception exception,
            HttpServletRequest request) {

        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "The product is being updated by another transaction. Please try again.",
                request
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, String details, HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(httpStatus, details);
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}
