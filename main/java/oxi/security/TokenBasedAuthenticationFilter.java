package oxi.security;

import oxi.models.User;
import oxi.models.Company;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.Set;
import java.util.Iterator;

import org.springframework.util.Assert;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static oxi.security.SecurityConfiguration.*;
import java.util.Base64;

public class TokenBasedAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private static final Logger logger = LogManager.getLogger(TokenBasedAuthenticationFilter.class);
	private boolean isCompany = false;
	private Set<AntPathRequestMatcher> requiresAuthenticationRequestMatchers = null;
	//@Value("${jwt.secret}")
	private String secret;
	//private final TokenAuthenticationService tokenAuthenticationService;
	//private final UserDetailsService userDetailsService;

	TokenBasedAuthenticationFilter(AuthenticationManager authenticationManager, boolean isCompany, String secret){
		super();
		this.isCompany = isCompany;
		this.secret = secret;
		setAuthenticationManager(authenticationManager);
	}

	/**
	*
	*/
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response){
		boolean isMatched = false;

		if(requiresAuthenticationRequestMatchers != null){
			Iterator<AntPathRequestMatcher> itr = requiresAuthenticationRequestMatchers.iterator();
	
			while(itr.hasNext()){
				if(itr.next().matches(request)){
					isMatched = true;
					break;
				}
			}
		}
		else{
			isMatched = super.requiresAuthentication(request, response);
		}

		return isMatched;
	}

	/**
	*Sets multiple URLs that determines if authentication is required.
	*/
	public final void setRequiresAuthenticationRequestMatchers(Set<AntPathRequestMatcher> requestMatchers){
		Assert.notNull(requestMatchers, "requestMaters cannot be null");
		this.requiresAuthenticationRequestMatchers = requestMatchers;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
		logger.debug("attempting Authentication");
		try{
			final User user;// = new ObjectMapper().readValue(request.getInputStream(), User.class);
			final Company company;
			final UsernamePasswordAuthenticationToken loginToken;// = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			if(isCompany){
				company = new ObjectMapper().readValue(request.getInputStream(), Company.class);
				logger.debug("TokenBasedAuthenticationFilter#attemptAuthentication companyName = " + company.getCompanyName());
				loginToken = new UsernamePasswordAuthenticationToken(company.getCompanyName(), company.getPassword());
			}
			else{
				user = new ObjectMapper().readValue(request.getInputStream(), User.class);
				logger.debug("TokenBasedAuthenticationFilter#attemptAuthentication username = " + user.getUsername());
				loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());				
			}


			//logger.debug("username = " + user.getUsername());
			//logger.debug("password = "+ user.getPassword());
			//logger.debug("loginToken = " + loginToken.toString());

			Authentication authentication = getAuthenticationManager().authenticate(loginToken);

			logger.debug("isAuthenticated = " + authentication.isAuthenticated());

			return authentication;
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		//return null;//TODO:  make sure this is ok
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException{
		logger.debug("Authentication secret = " + secret);
		String token = Jwts.builder()
			.setId(UUID.randomUUID().toString())
			.setSubject(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername())
			.setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
			.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
			.compact();
		
		response.addHeader(HttpHeaders.AUTHORIZATION, token);
		//Need to add the authorization scheme to the WWW-Authenticate response header so the user agent can construct a valid authentication header for subsequent requests
		response.addHeader("WWW-Authenticate", TOKEN_PREFIX);

		//SecurityContextHolder.getContext().seAuthentication(token)
	}

	public void setSecret(String secret){
		this.secret = secret;
	}
}