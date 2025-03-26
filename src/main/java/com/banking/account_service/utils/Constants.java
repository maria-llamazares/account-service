package com.banking.account_service.utils;

public class Constants {

    private Constants() {}

    // Error Codes
    public static final String ERROR_INSUFFICIENT_FUNDS = "ERROR_INSUFFICIENT_FUNDS";
    public static final String ERROR_URL_NOT_FOUND = "ERROR_URL_NOT_FOUND";
    public static final String ERROR_INPUT_VALIDATION = "ERROR_INPUT_VALIDATION";
    public static final String ERROR_PARAMETER_FORMAT_INVALID = "ERROR_PARAMETER_FORMAT_INVALID";
    public static final String ERROR_IBAN_NOT_FOUND = "IBAN_NOT_FOUND";
    public static final String ERROR_CURRENCY_NOT_FOUND = "CURRENCY_NOT_FOUND";
    public static final String ERROR_INVALID_CURRENCY = "ERROR_INVALID_CURRENCY";

    // Input Validation Error Messages
    public static final String MESSAGE_AMOUNT_REQUIRED = "The 'amount' parameter is required.";
    public static final String MESSAGE_AMOUNT_MINIMUM = "The 'amount' minimum value is 10.";
    public static final String MESSAGE_CURRENCY_REQUIRED = "The 'currency' parameter is required.";

    public static final int CODE_ERROR_NOT_FOUND = 404;
    public static final int CODE_ERROR_BAD_REQUEST = 400;
    public static final int CODE_ERROR_UNPROCESSABLE_ENTITY = 422;

}
