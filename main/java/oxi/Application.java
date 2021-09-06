package oxi;

import oxi.configs.*;
import oxi.services.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.*;
import org.springframework.boot.autoconfigure.jackson.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.*;
import org.springframework.web.servlet.config.annotation.*;//latest addition
import org.springframework.http.*;
import org.springframework.web.servlet.*;//latest addition
import org.springframework.web.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.context.annotation.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.core.io.*;
import org.springframework.data.rest.webmvc.config.*;
import org.springframework.data.web.config.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.data.repository.query.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.security.config.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.*;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import com.allanditzel.springframework.security.web.csrf.*;
import oxi.controllers.*;
import oxi.models.dto.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import oxi.components.CursorHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.Arrays;
import org.apache.catalina.connector.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//import org.springframework.boot.web.support.SpringBootServletInitializer;

//import org.springframework.boot.context.embedded.*;

//import org.springframework.boot.context.embedded.tomcat.*;





//import org.springframework.security.core;
//import org.springframework.security.access;
//import org.springframework.security.authentication;
//import org.springframework.security.provisioning;
//import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;




//import org.springframework.core.annotation.Order;


//import org.springframework.data.mongodb.core.*;
//import com.mongodb.MongoClient;




//Standard annotions for any spring boot configuration class
//@Order(1)
@Configuration
@ComponentScan(basePackages = {"oxi.configs, oxi.controllers, oxi.models, oxi.services, oxi.util.assemblers, oxi.models.dto, oxi.security, oxi.components, oxi.listeners"})//removed oxi.security
@EnableAutoConfiguration
@EntityScan(basePackages="oxi.models")
//@SpringBootApplication
//Addition annotations
//@EnableSpringDataWebSupport  // causes Exception:  The bean 'pageableResolver', defined in class path resource [org/springframework/data/rest/webmvc/config/RepositoryRestMvcConfiguration.class], ... could not be registered.
//@EnableJpaRepositories(basePackages="oxi.repositories"/*, queryLookupStrategy=QueryLookupStrategy.Key.USE_DECLARED_QUERY*/)
//@EnableWebSecurity
@EnableHypermediaSupport(type = HypermediaType.HAL)
@EnableWebMvc
@ImportResource("/WEB-INF/classes/applicationContext.xml")
public class Application extends SpringBootServletInitializer/* implements WebMvcConfigurer*/{

    //@Autowired
    //private ObjectMapper _halObjectMapper;

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(Application.class);
    }

    //@Override 
    //public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
    //    argumentResolvers.add(new CursorHandlerMethodArgumentResolver());
    //}

    //@Override 
    //public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
    //    logger.debug("configuring CursorHandlerMethodArgumentResolver");
    //    argumentResolvers.add(new CursorHandlerMethodArgumentResolver());
    //}
}