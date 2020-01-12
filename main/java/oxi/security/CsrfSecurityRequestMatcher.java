package oxi.security;

import org.springframework.security.web.util.matcher.RequestMatcher;
import java.util.regex.Pattern;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS|POST|PUT|PATCH)$");
    private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/account/user/register|/login|/logout|/consumer/outfits/.|consumer/.|.", null);
    private static final Logger logger = LogManager.getLogger(AjaxTimeoutRedirectFilter.class);

    //Returns boolean indicating whether the request should have CSRF token validated
    @Override
    public boolean matches(HttpServletRequest request) {
        if(allowedMethods.matcher(request.getMethod()).matches()){
            return false;
        }
        boolean csrfRequired = !unprotectedMatcher.matches(request);
        logger.debug("Is CSRF token required for this request: " + csrfRequired);
        return !unprotectedMatcher.matches(request);
    }
}