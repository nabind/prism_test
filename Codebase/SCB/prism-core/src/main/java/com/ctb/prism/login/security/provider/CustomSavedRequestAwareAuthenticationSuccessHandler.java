package com.ctb.prism.login.security.provider;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.login.security.filter.NoRedirectStrategy;

public class CustomSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	private RequestCache requestCache = new HttpSessionRequestCache();
	private String targetUrl;
	private boolean alwaysUseDefaultTargetUrl = false;
	
	public void setAlwaysUseDefaultTargetUrl(boolean alwaysUseDefaultTargetUrl) {
		this.alwaysUseDefaultTargetUrl = alwaysUseDefaultTargetUrl;
	}

	protected boolean isAlwaysUseDefaultTargetUrl() {
		return alwaysUseDefaultTargetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		super.setDefaultTargetUrl(targetUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		if( (request.getQueryString() != null 
				&& (request.getQueryString().endsWith("wsdl") || request.getQueryString().startsWith("xsd")) ) 
				|| IApplicationConstants.SOAP.equals(request.getHeader(IApplicationConstants.CLIENT_TYPE))
				/* ## new for eR candidate report (rest or section)*/
				|| IApplicationConstants.REST.equalsIgnoreCase(request.getParameter(IApplicationConstants.CLIENT_TYPE))) {
			// Upon successful authentication, Spring will attempt to try and move you to another URL 
	        // We have to prevent this because the request for the resource and the authentication all get done in the same request! 
			setRedirectStrategy(new NoRedirectStrategy());
		} else {
			setRedirectStrategy(new DefaultRedirectStrategy());
		}
		
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
	
	public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
