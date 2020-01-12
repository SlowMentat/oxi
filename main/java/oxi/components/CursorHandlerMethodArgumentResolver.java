package oxi.components;

import oxi.models.dto.CursorDto;

import java.util.regex.Pattern;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.stereotype.Component;

import javax.persistence.Transient;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public final class CursorHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Transient
    private static final Logger logger = LogManager.getLogger(CursorHandlerMethodArgumentResolver.class);

   //private static Pattern ISO_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d2:\\d2:\\d2.\\d2Z$");
   //private static Pattern UUID_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}");
   //private static Pattern SIZE_PATTERN = Pattern.compile("^\\d{1,3}$");

    private static String ISO_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d2:\\d2:\\d2.\\d2Z$";
    private static String UUID_ID_PATTERN = "^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}";
    private static String SIZE_PATTERN = "^\\d{1,3}$";
    //private static String DIRECTION_PATTERN = "^\\d{1}$";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        logger.debug("hello?");
        return methodParameter.getParameterType().equals(CursorDto.class);
    }
 
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        String direction = nativeWebRequest.getParameter("direction");
        String first = nativeWebRequest.getParameter("first");
        String size = nativeWebRequest.getParameter("size");
        String date = nativeWebRequest.getParameter("date");
        String firstId = nativeWebRequest.getParameter("firstId");
        String lastId = nativeWebRequest.getParameter("lastId");

        direction = direction != null ? direction : "0";
        first = Pattern.matches(SIZE_PATTERN, first) ? first : "10";
        size = Pattern.matches(SIZE_PATTERN, size) ? size : "10";
        date = Pattern.matches(ISO_DATE_PATTERN, date) ? date : "2019-12-27T04:48:18.657Z";
        firstId = Pattern.matches(UUID_ID_PATTERN, firstId) ? firstId : null;
        lastId = Pattern.matches(UUID_ID_PATTERN, lastId) ? lastId : null;

        return new CursorDto(Integer.parseInt(direction), Integer.parseInt(first), Integer.parseInt(size), firstId, lastId, date);
    }
}
