package com.security.exception;

import com.security.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class InvalidUserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorResponse errorResponse =
                new ErrorResponse(authException.getMessage(), HttpServletResponse.SC_UNAUTHORIZED, LocalDateTime.now());

        String jsonResponse = String.format(
                "{\"message\": \"%s\", \"statusCode\": %d, \"timestamp\": \"%s\"}",
                errorResponse.getMessage(),
                errorResponse.getStatusCode(),
                errorResponse.getTimestamp().toString());

        response.getWriter().write(jsonResponse);
    }
}
