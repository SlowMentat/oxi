package oxi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;

/*
We take advantage of this processor to avoid having to write the whole process of 
converting requests into classes. For those who are used to Spring MVC, the class 
extended is the one that process and populates @RequestBody parameters. This means 
that it takes, e.g., a JSON body and transforms on an instance of a class. In our 
case we tweak the base class to populate an instance of the DTO instead.
*/
public class DTOModelMapper extends RequestResponseBodyMethodProcessor {
    //This instance is used to map all DTOs into entities.
    private static final ModelMapper modelMapper = new ModelMapper();

    /*
    We inject an entity manager in this class to be able to query the database for 
    existing entities based on the id passed through DTOs.
    */
    private EntityManager entityManager;

    public DTOModelMapper(ObjectMapper objectMapper, EntityManager entityManager) {
        super(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
        this.entityManager = entityManager;
    }

    /*
    We overwrite the supportsParameter method. Without overwriting this method, our 
    new class would be applied for @RequestBody parameters, just like the base class. 
    Therefore we need to tweak it to make it apply for @DTO annotations only.
    */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DTO.class);
    }
    /*
    We overwrite validateIfApplicable. The base class runs bean validation only if 
    the parameter is marked with @Valid or @Validated. We change this behavior to 
    apply bean validation on all DTOs.
    */
    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        binder.validate();
    }

    /*
    We overwrite resolveArgument. This is the most important method in our 
    implementation. We tweak it to embed the ModelMapper instance in the process and 
    make it map DTOs into entities. But before mapping, we check if we are handling 
    a new entity, or if we have to apply the changes proposed by the DTO in an 
    existing entity.
    */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object dto = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        Object id = getEntityId(dto);
        if (id == null) {
            return modelMapper.map(dto, parameter.getParameterType());
        } else {
            Object persistedObject = entityManager.find(parameter.getParameterType(), id);
            modelMapper.map(dto, persistedObject);
            return persistedObject;
        }
    }

    /*
    We overwrite the readWithMessageConverters method. The base class simply takes the 
    parameter type and converts the request into an instance of it.  We overwrite this 
    method to make the conversion to the type defined in the DTO annotation, and leave 
    the mapping from the DTO to the entity to the resolveArgument method.
    */
    @Override
    protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        for (Annotation ann : parameter.getParameterAnnotations()) {
            DTO dtoType = AnnotationUtils.getAnnotation(ann, DTO.class);
            if (dtoType != null) {
                return super.readWithMessageConverters(inputMessage, parameter, dtoType.value());
            }
        }
        throw new RuntimeException();
    }

    /*
    We define a getEntityId method. This method iterates over the fields of the DTO 
    being populate to check if there is one marked with @Id. If it finds, it returns 
    the value of the field so resolveArgument can query the database with it.
    */
    private Object getEntityId(@NotNull Object dto) {
        for (Field field : dto.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.setAccessible(true);
                    return field.get(dto);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}