package com.banking.account_service.error;

import com.banking.account_service.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException mockMethodArgumentException;

    @Mock
    private BindingResult mockBindingResult;

    @Mock
    private FieldError mockFieldError;

    @Test
    void testHandleCustomExceptionTest() {

        CustomException customException = new CustomException(
                Constants.MESSAGE_CURRENCY_REQUIRED,
                HttpStatus.BAD_REQUEST.value(),
                Constants.ERROR_INVALID_CURRENCY);

        when(webRequest.getDescription(false)).thenReturn("uri=/accounts/iban/balances");

        ResponseEntity<CustomErrorResponse> response = globalExceptionHandler.handleCustomException(customException, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        customException = new CustomException(
                Constants.MESSAGE_CURRENCY_REQUIRED,
                HttpStatus.NOT_FOUND.value(),
                Constants.ERROR_INVALID_CURRENCY);

        response = globalExceptionHandler.handleCustomException(customException, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        customException = new CustomException(
                Constants.MESSAGE_CURRENCY_REQUIRED,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Constants.ERROR_INVALID_CURRENCY);

        response = globalExceptionHandler.handleCustomException(customException, webRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    void testHandleUrlNotFoundTest() {

        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/accounts/iban/balances", new HttpHeaders());
        ResponseEntity<CustomErrorResponse> response = globalExceptionHandler.handleUrlNotFound(ex, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

   @Test
    void testHandleValidationInputParams() {

        when(mockFieldError.getDefaultMessage()).thenReturn("Invalid field");
        when(mockBindingResult.getAllErrors()).thenReturn(Collections.singletonList(mockFieldError));
        when(mockMethodArgumentException.getBindingResult()).thenReturn(mockBindingResult);

        ResponseEntity<CustomErrorResponse> response = globalExceptionHandler.handleValidationInputParams(mockMethodArgumentException, webRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}