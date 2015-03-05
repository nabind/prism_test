package com.ctb.prism.login.security.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.filter.NoRedirectStrategy;

@Component
public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private String failureUrl;
	private boolean forwardToDestination = false;
	private boolean isWebService = false;
	
	@Autowired private ILoginService loginService;
	@Autowired private CookieThemeResolver themeResolver;
	
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
		boolean ssoRedirect = false;
		String redirectUrl = null;
		if(IApplicationConstants.SOAP.equals(request.getHeader(IApplicationConstants.CLIENT_TYPE))
				/* ## new for eR candidate report (or section)*/
				|| IApplicationConstants.REST.equals(request.getHeader(IApplicationConstants.CLIENT_TYPE))) {
			// Upon successful authentication, Spring will attempt to try and move you to another URL 
	        // We have to prevent this because the request for the resource and the authentication all get done in the same request! 
			setRedirectStrategy(new NoRedirectStrategy());
			isWebService = true;
		} else {
			try {
				String applicationName = getHeaderValue(request, "application_name");
				String signature = getHeaderValue(request, "signature");
				if(!StringUtils.isEmpty(signature) || !StringUtils.isEmpty(applicationName)) {
					// login failure during SSO
					String contractName = Utils.getContractName(themeResolver.resolveThemeName(request));
					Map<String, Object> tileParamMap = new HashMap<String, Object>();
					tileParamMap.put("contractName", contractName);
					Map<String, Object> propertyMap = loginService.getContractProerty(tileParamMap);
					redirectUrl = (String)propertyMap.get(IApplicationConstants.SSO_REDIRECT_LOGINFAIL+"~"+CustomStringUtil.appendString(applicationName, "|", contractName));
					if(!StringUtils.isEmpty(redirectUrl)) {
						ssoRedirect = true;
					} else {
						setRedirectStrategy(new DefaultRedirectStrategy());
					}
				} else {
					// login failure from login page
					setRedirectStrategy(new DefaultRedirectStrategy());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				// fall back
				setRedirectStrategy(new DefaultRedirectStrategy());
			}
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
            	if(ssoRedirect) {
            		logger.info("Redirecting to " + redirectUrl);
            		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            	} else {
            		logger.info("Redirecting to " + failureUrl);
            		getRedirectStrategy().sendRedirect(request, response, failureUrl);
            	}
                if(isWebService) response.getWriter().write("Unauthorized request!!");
            }
        }
        
    }
	
	private String getHeaderValue(HttpServletRequest request, String headerParameterName) {
    	return (request.getHeader(headerParameterName) != null) ? 
        		request.getHeader(headerParameterName) : getParameterValue(request, headerParameterName);
    }
    
    private String getParameterValue(HttpServletRequest request, String requestParameterName) {
        return (request.getParameter(requestParameterName) != null) ? request.getParameter(requestParameterName) : "";
    }
}
