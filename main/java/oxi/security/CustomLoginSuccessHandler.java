package oxi.security;

import oxi.services.UserAccountService;
import org.springframework.security.web.authentication.*;
import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.*;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



//public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LogManager.getLogger(UserAccountService.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        super();
        logger.debug("CustomLoginSuccessHandler # constructor: instantiating class");
        //setDefaultTargetUrl(defaultTargetUrl);
    }

    /**{@inheritDoc}*/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        logger.debug("CustomLoginSuccessHandler # onAuthenticationSuccess:  invoked");
        //HttpSession session = request.getSession();
        /*
        if(session != null){
            String redirectUrl = (String) session.getAttribute("url_prior_login");

            if(redirectUrl != null){
                logger.debug("CustomLoginSuccessHandler # onAuthenticationSuccess: redirectUrl = " + redirectUrl);
                // we do not forget to clean this attribute from session
                session.removeAttribute("url_prior_login");
                // then we redirect
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } 
            else{
                logger.debug("CustomLoginSuccessHandler # onAuthenticationSuccess: redirectUrl is null");
                super.onAuthenticationSuccess(request, response, authentication);
            }

        }
        else{
            logger.debug("CustomLoginSuccessHandler # onAuthenticationSuccess: session is null");
            super.onAuthenticationSuccess(request, response, authentication);
        }
        */
        handle(request, response, authentication);
    }

    /**
    *Invoked by {@link onAuthenticationSuccess}. 
    *<p>
    * Any tasks to be done after successfull authentication are invoked from this function
    *</p>
    *@param request - the request which caused the successful authentication
    *@param response - the response
    *@param authentication - the Authentication object which was created during the authentication process
    *@throws IOException
    */
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetURL = determineTargetURL(request.getRequestURI());

        if(response.isCommitted()) {
            logger.debug("Response has already been comitted.  Unable to redirect to " + targetURL);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetURL);
    }

    /**
    *Determines the target return url after successfull authentication
    *<p>
    *The return url depends on the authenticating request URI.
    *<br/>Authentication requests made to <b>/consumer/login</b> are redirected to the {@link defaultTargetUrl}
    *<br/>Authentication requests made to <b>/account/user/sendVerificationEmail</b> are redirected to <b>/account/user/sendVerificationEmail</b>
    *</p>
    *@param requestURI - the request which caused the successful authentication
    *@return the target URL to redirect to after on successfull authentication
    */
    protected String determineTargetURL(String requestURI) {
        String targetURL = null;
        logger.debug("CustomLoginSuccessHandler # determineTargetURL:  requestURI = " + requestURI);

        switch(requestURI){
            case "/consumer/login" :
                targetURL = "/shop/browse";
                break;

            case "/account/user/login" :
                targetURL = "/account/user/sendVerificationEmail";
                break;

            default:
                //targetURL = getDefaultTargetUrl();
                break;
        }

        logger.debug("redirect URL is " + targetURL);
        return targetURL;
    }
}