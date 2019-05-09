package oxi.security;

import io.jsonwebtoken.Jwts;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static oxi.security.SecurityConfiguration.*;

public class TokenBasedAuthorizationFilter extends BasicAuthenticationFilter{

	private static final Logger logger = LogManager.getLogger(TokenBasedAuthorizationFilter.class);


	TokenBasedAuthorizationFilter(AuthenticationManager authenticationManager){
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
		
		String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		//String authorizationToken = request.getHeader("Authorization");
		//String authorizationToken = request.getAuthType();
		Enumeration<String> headerNames = request.getHeaderNames();
		logger.debug("Request Headers:");
		while(headerNames.hasMoreElements()){
			logger.debug("	" + headerNames.nextElement());
		}

		logger.debug("authorizationToken = " + authorizationToken);

		if(authorizationToken != null && authorizationToken.startsWith(TOKEN_PREFIX)){
			authorizationToken = authorizationToken.replaceFirst(TOKEN_PREFIX, "");
			String username = Jwts.parser()
				.setSigningKey(TOKEN_SECRET)
				.parseClaimsJws(authorizationToken)
				.getBody()
				.getSubject();

			logger.debug("username = " + username);

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()));
		}

		chain.doFilter(request, response);
	}
}