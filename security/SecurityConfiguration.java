package oxi.security;


import oxi.services.CustomUserDetailsService;
import oxi.services.CustomCompanyDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.core.annotation.*;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.context.annotation.ComponentScan;



@Configuration
@EnableWebSecurity
//@Order(1)
@ComponentScan(basePackages = {"oxi.security"})
public class SecurityConfiguration{

	public static final String CSRF_COOKIE = "CSRF-TOKEN";
	public static final String CSRF_HEADER = "X-CSRF-TOKEN";
	static public final String TOKEN_PREFIX = "Bearer ";
	static final long TOKEN_LIFETIME = 604_800_000;
	static final String TOKEN_SECRET = Base64.getEncoder().encodeToString("ThisIsOurSecretKeyToSignOurTokens".getBytes());

	@Configuration
	@Order(1)
	public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Resource
		private AuthenticationEntryPoint authenticationEntryPoint;
	
		private AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
	
		//@Autowired
	    //private CustomLoginSuccessHandler CustomLoginSuccessHandler;
	
		//@Resource
		private AuthenticationSuccessHandler authenticationSuccesshandler = new CustomLoginSuccessHandler("https://www.oxisalechannel.com/gs-convert-jar-to-war-0.1.0/consumer/profile");
	    //@Bean
	    //public AuthenticationSuccessHandler authenticationSuccessHandler (){
	    //	return new CustomLoginSuccessHandler("https://www.oxisalechannel.com/gs-convert-jar-to-war-0.1.0/consumer/profile");
	    //}
	
	   	//@Resource
	   	//private LogoutSuccessHandler logoutSuccessHandler;
	
		private StatelessCsrfFilter statelessCsrfFilter = new StatelessCsrfFilter();
	
		@Resource
		private CustomUserDetailsService customUserDetailsService;
	
		@Bean
		public AccessDeniedHandler userAccessDeniedHandler() {
			return new AccessDeniedHandlerImpl();
		}
	
		@Bean
		public BCryptPasswordEncoder userPasswordEncoder() throws NoSuchAlgorithmException {
			return new BCryptPasswordEncoder();
		}
	
		//@Bean
		//public PasswordEncoder getEncoder() {
		//    return new BCryptPasswordEncoder();
		//}
	
	
	   /* @Autowired 
	    private CustomUserDetailsService customUserDetailsService;
	
	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;*/
	
	    /*@Bean
	    public DaoAuthenticationProvider authProvider(){
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(customUserDetailsService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }*/
	
	
		//@Autowired
		@Override
		protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(userPasswordEncoder());
		}
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	
			http
					.csrf().disable();
					//.addFilterBefore(statelessCsrfFilter, CsrfFilter.class);
	
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
	       	http.formLogin().successHandler(authenticationSuccesshandler);
	        http.formLogin().failureHandler(authenticationFailureHandler);
			//http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
	
			http
					//.cors().and()
					//.httpBasic().and()
					.addFilterBefore(new TokenBasedAuthenticationFilter(authenticationManager(), false), UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(new TokenBasedAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
	        		// this disables session creation on Spring Security
	        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
			http
					//.exceptionHandling().and()
					.anonymous().and()
					.servletApi().and()
					.headers().cacheControl();
					
			http
					.authorizeRequests()
					//.antMatchers("/").permitAll()
					.antMatchers("/login").permitAll()
					.antMatchers("/account/user/register").permitAll()
					.antMatchers("/account/retailer/register").permitAll()
					.antMatchers("/account/user/confirm").permitAll() // TODO:  eventually change this to HasRole
					//.antMatchers(HttpMethod.GET "/**").permitAll()
					.antMatchers("/consumer/**").authenticated();
					//.anyReqyest().hasRole("USER");
		}
		////TODO:  change cors to only trust source from apache web server porxy
		//@Bean
		//CorsConfigurationSource corsConfigurationSource() {
		//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		//		return source;
		//}
	}


	@Configuration
	@Order(2)
	public class RetailerSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
		//public static final String CSRF_COOKIE = "CSRF-TOKEN";
		//public static final String CSRF_HEADER = "X-CSRF-TOKEN";
	
		//static final long TOKEN_LIFETIME = 604_800_000;
		//static final String TOKEN_SECRET = Base64.getEncoder().encodeToString("ThisIsOurCompanySecretKeyToSignOurTokens".getBytes());
	
		@Resource
		private AuthenticationEntryPoint authenticationEntryPoint;	
		private AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
		private AuthenticationSuccessHandler authenticationSuccesshandler = new CustomLoginSuccessHandler("https://www.oxisalechannel.com/gs-convert-jar-to-war-0.1.0/consumer/profile");
		private StatelessCsrfFilter statelessCsrfFilter = new StatelessCsrfFilter();
	
		@Resource
		private CustomCompanyDetailsService customCompanyDetailsService;
	
		@Bean
		public AccessDeniedHandler companyAccessDeniedHandler() {
			return new AccessDeniedHandlerImpl();
		}
	
		@Bean
		public BCryptPasswordEncoder companyPasswordEncoder() throws NoSuchAlgorithmException {
			return new BCryptPasswordEncoder();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			authenticationManagerBuilder.userDetailsService(customCompanyDetailsService).passwordEncoder(companyPasswordEncoder());
		}
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	
			http
					.csrf().disable();
					//.addFilterBefore(statelessCsrfFilter, CsrfFilter.class);
	
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
	       	http.formLogin().successHandler(authenticationSuccesshandler);
	        http.formLogin().failureHandler(authenticationFailureHandler);
			//http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
	
			http
					//.cors().and()
					//.httpBasic().and()
					.addFilterBefore(new TokenBasedAuthenticationFilter(authenticationManager(), true), UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(new TokenBasedAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
	        		// this disables session creation on Spring Security
	        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
			http
					//.exceptionHandling().and()
					.anonymous().and()
					.servletApi().and()
					.headers().cacheControl();
					
			http
					.authorizeRequests()
					//.antMatchers("/").permitAll()
					.antMatchers("/login").permitAll()
					.antMatchers("/account/user/register").permitAll()
					.antMatchers("/account/retailer/register").permitAll()
					.antMatchers("/account/user/confirm").permitAll() // TODO:  eventually change this to HasRole
					//.antMatchers(HttpMethod.GET "/**").permitAll()
					.antMatchers("/retailer/**").authenticated();
					//.anyReqyest().hasRole("USER");
		}
	}

	//TODO:  change cors to only trust source from apache web server porxy
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
			final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
			return source;
	}
}