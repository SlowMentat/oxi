package oxi;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.*;//latest addition
import org.springframework.web.servlet.*;//latest addition
import org.springframework.web.*;
import org.springframework.boot.orm.jpa.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.context.annotation.*;

import org.springframework.core.io.*;

import org.springframework.data.rest.webmvc.config.*;
import org.springframework.data.jpa.repository.config.*;

//import org.springframework.security.core;
//import org.springframework.security.access;
//import org.springframework.security.authentication;
//import org.springframework.security.provisioning;
import org.springframework.security.config.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.config.annotation.web.configuration.*;
//import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.core.session.SessionRegistryImpl;

import com.allanditzel.springframework.security.web.csrf.*;

import java.util.*;

//Standard annotions for any spring boot configuration class
@Configuration
@ComponentScan(basePackages = {"classpath:controllers,classpath:models,classpath:security,classpath:repositories"})
@EnableAutoConfiguration
//Addition annotations
@ImportResource("/WEB-INF/classes/applicationContext.xml")
@EntityScan(basePackages="classpath:models")
@EnableJpaRepositories(basePackages="classpath:repositories")
@EnableWebSecurity
//@EnableWebMvc
//@Import(RepositoryRestMvcConfiguration.class)
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
        //return application.sources(new ClassPathXmlApplicationContext("resources/applicationContext.xml"));
    }
}