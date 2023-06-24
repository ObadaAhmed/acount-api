package com.accountapi.api.security.exceptions;

import com.accountapi.api.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import java.io.IOException;

@Component
public class JwtAthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse body = new ErrorResponse();
        try {
            body.setApplicationMessage(request.getAttribute("errorMessage").toString() + "");
            body.setErrorMessage(request.getAttribute("errorMessage").toString() + "");
            body.setLocalizedErrorMessage(request.getAttribute("errorMessage").toString() + "");
            body.setErrorCode(request.getAttribute("errorCode").toString() + "");
        }
        catch (Exception e){
            logger.error("Error mapping the errorResponse " + e.getMessage());
            body.setApplicationMessage("JWT authentication failure");
            body.setErrorMessage("UnAuthorized");
            body.setLocalizedErrorMessage("JWT authentication failure");
            body.setErrorCode("401");
        }
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}