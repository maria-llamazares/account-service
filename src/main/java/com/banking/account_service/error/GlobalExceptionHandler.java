package com.banking.account_service.error;

import com.banking.account_service.utils.Constants;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles exceptions of type {@link CustomException}.
     * Constructs a {@link CustomErrorResponse} based on the exception details
     * and the HTTP status determined by the exception's status.
     *
     * @param ex the exception of type {@link CustomException} that has occurred
     * @param request the {@link WebRequest} containing request details
     * @return a {@link ResponseEntity} containing the {@link CustomErrorResponse}
     *         and the corresponding HTTP status
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex, WebRequest request) {

        HttpStatus status = switch (ex.getStatus()) {
            case Constants.CODE_ERROR_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case Constants.CODE_ERROR_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case Constants.CODE_ERROR_UNPROCESSABLE_ENTITY -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                status.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handles the {@link NoHandlerFoundException} triggered when a requested URL is not found.
     * Constructs a {@link CustomErrorResponse} containing details such as the timestamp,
     * HTTP status, error code, exception message, and request path.
     *
     * @param ex the {@link NoHandlerFoundException} that was thrown when the URL could not be found
     * @param request the {@link WebRequest} containing details of the originating HTTP request
     * @return a {@link ResponseEntity} containing the {@link CustomErrorResponse}
     *         with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUrlNotFound(NoHandlerFoundException ex, WebRequest request) {

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                Constants.ERROR_URL_NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Handles exceptions of type {@link MethodArgumentNotValidException}.
     * This method is triggered when input parameters annotated with {@link jakarta.validation.Valid}
     * fail validation due to not meeting the defined constraints.
     * It retrieves details about the field errors, including the field name and the specific issue,
     * and constructs a {@link CustomErrorResponse} containing the timestamp, HTTP status, error code,
     * error message, and request path.
     *
     * @param ex the {@link MethodArgumentNotValidException} that occurred due to invalid input parameters
     * @param request the {@link WebRequest} containing details of the originating HTTP request
     * @return a {@link ResponseEntity} containing the {@link CustomErrorResponse} with
     *         HTTP status 400 (Bad Request) and additional error details
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationInputParams(MethodArgumentNotValidException ex, WebRequest request) {

        FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().getFirst();

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                Constants.ERROR_INPUT_VALIDATION,
                fieldError.getDefaultMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type {@link HttpMessageNotReadableException}.
     * This method is triggered when the JSON input provided in the request body cannot be parsed or mapped
     * to the expected data structure. It extracts additional details such as the problematic field name
     * and expected format/type when available.
     * Constructs a {@link CustomErrorResponse} containing details such as the timestamp,
     * HTTP status, error code, a descriptive message, and the request path.
     *
     * @param ex the {@link HttpMessageNotReadableException} that was thrown due to an unreadable or invalid JSON input
     * @param request the {@link WebRequest} containing details of the originating HTTP request
     * @return a {@link ResponseEntity} containing the {@link CustomErrorResponse}
     *         with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidInput(HttpMessageNotReadableException ex, WebRequest request) {

        String parameter = "";
        String message = "";

        // If the exception has a cause that is a JsonMappingException, we retrieve the details
        if (ex.getCause() instanceof JsonMappingException mappingException) {

            // Retrieve the name of the problematic field
            parameter = mappingException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("unknown");

            // Here we attempt to retrieve the expected type from the error context
            message = mappingException.getOriginalMessage();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                Constants.ERROR_PARAMETER_FORMAT_INVALID,
                "Parameter: '"+ parameter +"'. "+message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
