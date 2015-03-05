package com.ctb.prism.login.security.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;

@Component (value = "CustomLogoutSuccessHandler")
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
		implements LogoutSuccessHandler {
	
	private static IAppLogger logger = LogFactory.getLoggerInstance(CustomLogoutSuccessHandler.class.getName());
	private static final String DEFAULT_LOGOUT_URL = "/userlogin.do";
	@Autowired private ILoginService loginService;
	@Autowired private CookieThemeResolver themeResolver;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.log(IAppLogger.INFO, "Logout Called: MyCustomLogoutSuccessHandler");
		super.setDefaultTargetUrl(DEFAULT_LOGOUT_URL);
		if(authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_SSO");
			if(authorities.contains(auth)) {
				String contractName = Utils.getContractName(themeResolver.resolveThemeName(request));
				String applicationName = "";
				Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					if("SSOAPP".equals(cookie.getName())) {
						applicationName = cookie.getValue();
						break;
					}
				}
				Map<String, Object> tileParamMap = new HashMap<String, Object>();
				tileParamMap.put("contractName", contractName);
				Map<String, Object> propertyMap = loginService.getContractProerty(tileParamMap);
				if(propertyMap.get(IApplicationConstants.SSO_REDIRECT_LOGOUT+"~"+CustomStringUtil.appendString(applicationName, "|", contractName)) != null) {
					String redirectUrl = (String) propertyMap.get(IApplicationConstants.SSO_REDIRECT_LOGINFAIL+"~"+CustomStringUtil.appendString(applicationName, "|", contractName));
					if(!StringUtils.isEmpty(redirectUrl)) super.setDefaultTargetUrl(redirectUrl);
				}
			}
		}
		super.onLogoutSuccess(request, response, authentication);
	}
}
