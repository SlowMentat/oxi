package oxi;

import oxi.configs.*;
import oxi.services.CustomUserDetailsService;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.*;
import org.springframework.boot.autoconfigure.jackson.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.servlet.config.annotation.*;//latest addition
import org.springframework.web.servlet.*;//latest addition
import org.springframework.web.*;

import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.context.annotation.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.core.io.*;

import org.springframework.data.rest.webmvc.config.*;
import org.springframework.data.web.config.*;
import org.springframework.data.jpa.repository.config.*;

import com.fasterxml.jackson.databind.*;

//import org.springframework.security.core;
//import org.springframework.security.access;
//import org.springframework.security.authentication;
//import org.springframework.security.provisioning;
import org.springframework.security.config.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.config.annotation.web.configuration.*;
//import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.core.*;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import com.allanditzel.springframework.security.web.csrf.*;

import oxi.controllers.*;
import oxi.models.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.*;
import java.security.SecureRandom;

import java.util.*;
import java.util.Arrays;

//Standard annotions for any spring boot configuration class
@Configuration
@ComponentScan(basePackages = {"oxi.configs, oxi.controllers, oxi.models, oxi.repositories, oxi.services, oxi.util.assemblers, oxi.models.dto, oxi.security, oxi.components"})//removed oxi.security
@EnableAutoConfiguration
//@SpringBootApplication
//Addition annotations
@EnableSpringDataWebSupport
@EntityScan(basePackages="oxi.models")
@EnableJpaRepositories(basePackages="oxi.repositories")
@EnableWebSecurity
@EnableHypermediaSupport(type = HypermediaType.HAL)
//@EnableWebMvc
@ImportResource("/WEB-INF/classes/applicationContext.xml")
public class Application extends SpringBootServletInitializer{

    @Autowired
    private ObjectMapper _halObjectMapper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilder() {
        return builder -> builder.configure(_halObjectMapper);
    }

    @Bean 
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11, new SecureRandom());
    }


    //configuration for 
    @Autowired 
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /*@Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter byteArrayHttpMC = new ByteArrayHttpMessageConverter();
        byteArrayHttpMC.setSupportedMediaTypes(getImageMediaTypes());
        return byteArrayHttpMC;
    }*/
}