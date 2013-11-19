package com.ctb.prism.login.security.provider;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.login.security.filter.NoRedirectStrategy;

public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private String failureUrl;
	private boolean forwardToDestination = false;
	private boolean isWebService = false;
	
	public void setFailureUrl(String defaultFailureUrl) {
		this.failureUrl = defaultFailureUrl;
		super.setDefaultFailureUrl(failureUrl);
	}

	public CustomSimpleUrlAuthenticationFailureHandler() {
	}

	public CustomSimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
		super.setDefaultFailureUrl(defaultFailureUrl);
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
		
		if(IApplicationConstants.SOAP.equals(request.getHeader(IApplicationConstants.CLIENT_TYPE))) {
			// Upon successful authentication, Spring will attempt to try and move you to another URL 
	        // We have to prevent this because the request for the resource and the authentication all get done in the same request! 
			setRedirectStrategy(new NoRedirectStrategy());
			isWebService = true;
		} else {
			setRedirectStrategy(new DefaultRedirectStrategy());
		}

        if (failureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            saveException(request, exception);

            if (forwardToDestination) {
                logger.debug("Forwarding to " + failureUrl);

                request.getRequestDispatcher(failureUrl).forward(request, response);
            } else {
                logger.debug("Redirecting to " + failureUrl);
                getRedirectStrategy().sendRedirect(request, response, failureUrl);
                if(isWebService) response.getWriter().write("Unauthorized request!!");
            }
        }
        
    }
}
