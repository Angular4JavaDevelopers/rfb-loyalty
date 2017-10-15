package com.rfb.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RfbAjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Failed to sign in! Please check your credentials and try again.";

        if( exception.getMessage().equalsIgnoreCase("blocked") ) {
            errorMessage = "You have been blocked from our system for 10 invalid login attempts";
        }

        response.sendError(401, errorMessage);
    }

}
