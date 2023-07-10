package com.accountapi.api.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@ControllerAdvice
public class ExceptionHandlers {

    protected static Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);


    @Autowired
    private MessageResolverService messageResolverService;

    @ExceptionHandler(BaseWebApplicationException.class)
    public @ResponseBody
    ErrorResponse baseWebAppException(HttpServletResponse servletResponse, BaseWebApplicationException ex) {
        logException(ex);
        servletResponse.setStatus(ex.getStatus());
        ErrorResponse errorResponse = ex.getErrorResponse();
        errorResponse.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(ex));
        logger.debug("baseWebAppException setErrorMessage:" + errorResponse.getErrorMessage());
        logger.debug("baseWebAppException ex:" + ex);
            return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse ValidationError(HttpServletResponse servletResponse, MethodArgumentNotValidException ex) {
        logException(ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.BAD_REQUEST.value(), "001", "ex.method.args.invalid", "Validation Error", "The data passed in the request was invalid. Please check and resubmit");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
         response.setValidationErrors(processFieldErrors(fieldErrors));
        servletResponse.setStatus(exception.getStatus());
        return response;
    }

    @ExceptionHandler(RestClientException.class)
    public @ResponseBody
    ErrorResponse restClientException(HttpServletResponse servletResponse, RestClientException ex) {
        logException(ex);
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "001", "ex.rest.client.error", "Connection Error", "Error connecting with remote service");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
         servletResponse.setStatus(exception.getStatus());
        return response;
    }
    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ErrorResponse BadCreadientialsException(HttpServletResponse servletResponse, BadCredentialsException ex) {
        logException(ex);
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "001", "ex.rest.client.error", "Connection Error", "Error connecting with remote service");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
         servletResponse.setStatus(exception.getStatus());
        return response;
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public @ResponseBody ErrorResponse UserNotFoundException(HttpServletResponse servletResponse, InternalAuthenticationServiceException ex) {
        logException(ex);
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "004", "ex.rest.client.error", "no records found for specified user", "User not found");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
         servletResponse.setStatus(exception.getStatus());
        return response;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    public @ResponseBody
    ErrorResponse httpMethod(HttpServletResponse servletResponse, Exception ex) {
        logException(ex);
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "005", "ex.http.request.notSupported", "Request Error", "Http Content-Type or Method Not Supported");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
         servletResponse.setStatus(exception.getStatus());
        return response;

    }

    @ExceptionHandler(Throwable.class)
    public @ResponseBody
    ErrorResponse otherThrowable(HttpServletResponse servletResponse, Throwable e) {
        logException(e);
        ApplicationRuntimeException exception = new ApplicationRuntimeException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "006", "ex.default.system.error", "System Error", "System Error");
        ErrorResponse response = exception.getErrorResponse();
        response.setErrorMessage(messageResolverService.resolveLocalizedErrorMessage(exception));
        servletResponse.setStatus(exception.getStatus());
        return response;

    }

    private List<ValidationError> processFieldErrors(List<FieldError> fieldErrors) {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = messageResolverService.resolveLocalizedErrorMessage(fieldError);
            ValidationError error = new ValidationError();
            error.setMessage(localizedErrorMessage);
            error.setPropertyName(fieldError.getField());
            error.setPropertyValue(fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null);
            errors.add(error);
        }
        return errors;
    }

    private void logException(Throwable ex) {
        logger.error(ex.getLocalizedMessage(), ex);
    }
}
