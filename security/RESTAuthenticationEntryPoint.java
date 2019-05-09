package oxi.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static oxi.security.SecurityConfiguration.*;

@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
		response.addHeader(HttpHeaders.WWW_AUTHENTICATE, TOKEN_PREFIX);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}