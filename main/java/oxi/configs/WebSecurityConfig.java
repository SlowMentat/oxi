/*package oxi.configs;

import org.springframework.web.servlet.config.annotation.*;
import org.springframework.http.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

  /**
    *  Total customization - see below for explanation.
    */
  /*@Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorPathExtension(false).//Disabled path extension. Note that favor does not mean use one approach in preference to another, it just enables or disables it. The order of checking is always path extension, parameter, Accept header.
            favorParameter(true).
            parameterName("mediaType").//Enable the use of the URL parameter but instead of using the default parameter, format, we will use mediaType instead.
            ignoreAcceptHeader(true).//Ignore the Accept header completely. This is often the best approach if most of your clients are actually web-browsers (typically making REST calls via AJAX).
            useJaf(false).//Don't use the JAF, instead specify the media type mappings manually - we only wish to support JSON, XML, JPEG, and PNG.
            defaultContentType(MediaType.APPLICATION_JSON).
            mediaType("xml", MediaType.APPLICATION_XML).
            mediaType("json", MediaType.APPLICATION_JSON).
            mediaType("jpeg", MediaType.IMAGE_JPEG).
            mediaType("png", MediaType.IMAGE_PNG);//.
            //mediaType("octet-stream", MediaType.APPLICATION_OCTET_STREAM);
  }
}*/