package oxi.configs;

import com.fasterxml.jackson.databind.*;
import org.springframework.data.rest.webmvc.config.*;
import org.springframework.context.annotation.*;

@Configuration
public class RepositoryRestConfig /*extends RepositoryRestConfigurerAdapter*/ {
    /*@Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper){    	
    	objectMapper.disable(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES);
    }*/
}