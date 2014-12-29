package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserAuthorityChanger;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.provider.UserDetailsManager;
import com.ctb.prism.login.transferobject.UserTO;

public class CustomSwitchUserFilter extends GenericFilterBean implements
		ApplicationEventPublisherAware, MessageSourceAware {
	// ~ Static fields/initializers
	// =====================================================================================

	public static final String SPRING_SECURITY_SWITCH_USERNAME_KEY = "j_username";
	public static final String ROLE_PREVIOUS_ADMINISTRATOR = "ROLE_PREVIOUS_ADMINISTRATOR";

	// ~ Instance fields
	// ================================================================================================

	private ApplicationEventPublisher eventPublisher;
	private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();
	private String exitUserUrl = "/j_spring_security_exit_user";
	private String switchUserUrl = "/j_spring_security_switch_user";
	private String targetUrl;
	private String switchFailureUrl;
	private SwitchUserAuthorityChanger switchUserAuthorityChanger;
	private UserDetailsManager userDetailsService;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
	private AuthenticationSuccessHandler successHandler;
	private AuthenticationFailureHandler failureHandler;
	
	@Autowired
	private ILoginService loginService;

	// ~ Methods
	// ========================================================================================================

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(userDetailsService,
				"userDetailsService must be specified");
		Assert.isTrue(successHandler != null || targetUrl != null,
				"You must set either a successHandler or the targetUrl");
		if (targetUrl != null) {
			Assert.isNull(successHandler,
					"You cannot set both successHandler and targetUrl");
			successHandler = new SimpleUrlAuthenticationSuccessHandler(
					targetUrl);
		}

		if (failureHandler == null) {
			failureHandler = switchFailureUrl == null ? new SimpleUrlAuthenticationFailureHandler()
					: new SimpleUrlAuthenticationFailureHandler(
							switchFailureUrl);
		} else {
			Assert.isNull(switchFailureUrl,
					"You cannot set both a switchFailureUrl and a failureHandler");
		}
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// check for switch or exit request
		if (requiresSwitchUser(request)) {
			// if set, attempt switch and store original
			try {
				// keep previous user name in session
				/*request.getSession().setAttribute(IApplicationConstants.PREV_ADMIN, 
						request.getSession().getAttribute(IApplicationConstants.CURR_USER_DISPLAY));*/
				
				Authentication targetUser = attemptSwitchUser(request);
				/*Check if the current user has admin role to do loginAS
				 * if not then targetUser will be null */
				if(targetUser == null) {
					logger.debug("Switch User failed for malicious user loginAS");
					throw new Exception();
				}
				String prevAdmin = "Admin";
				for(GrantedAuthority ga : targetUser.getAuthorities()) {
					if(ga instanceof SwitchUserGrantedAuthority) {
						UserDetails   userDetails = (UserDetails)((SwitchUserGrantedAuthority) ga).getSource().getPrincipal();
						prevAdmin = userDetails.getUsername();
					}
				}
				//Get details of prev admin user details
				
				paramMap.put("username", prevAdmin);
				UserTO prevAdminuserDetails = loginService.getUserByEmail(paramMap);
				request.getSession().setAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME, prevAdminuserDetails.getDisplayName());
				request.getSession().setAttribute(IApplicationConstants.PREV_ADMIN, prevAdmin);
				// ADDED FOR SSO (and then using LOGIN-AS)
				String ssoOrg = (String) request.getSession().getAttribute(IApplicationConstants.SSO_ORG);
				if(ssoOrg != null && ssoOrg.length() > 0) {
					request.getSession().setAttribute(IApplicationConstants.SSO_PREV_ADMIN, ssoOrg);
				}

				// update the current context to the new target user
				SecurityContextHolder.getContext()
						.setAuthentication(targetUser);

				// redirect to target url
				successHandler.onAuthenticationSuccess(request, response,
						targetUser);
			} catch (AuthenticationException e) {
				logger.debug("Switch User failed", e);
				failureHandler.onAuthenticationFailure(request, response, e);
			} catch (SystemException e) {
				logger.debug("Switch User failed", e);
				e.printStackTrace();
			} catch (Exception e) {
				logger.debug("Switch User failed", e);
				failureHandler.onAuthenticationFailure(request, response, (AuthenticationException) e);
			}

			return;
		} else if (requiresExitUser(request)) {
			// remove previous admin user
			request.getSession().removeAttribute(IApplicationConstants.PREV_ADMIN);
			// Added for story(defect) 76956 by Abir
			request.getSession().removeAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME);
			request.getSession().removeAttribute(IApplicationConstants.SSO_PREV_ADMIN);
			
			// get the original authentication object (if exists)
			Authentication originalUser = attemptExitUser(request);
			
			Authentication prevAdminUser = getSourceAuthentication(originalUser);
			if(prevAdminUser != null) {
				UserDetails   userDetails = (UserDetails) prevAdminUser.getPrincipal();
				request.getSession().setAttribute(IApplicationConstants.PREV_ADMIN, userDetails.getUsername());
				//Get details of prev admin user details				
				try {
					paramMap.put("username", userDetails.getUsername());
					UserTO prevAdminuserDetails = loginService.getUserByEmail(paramMap);
					request.getSession().setAttribute(IApplicationConstants.PREV_ADMIN_DISPNAME, prevAdminuserDetails.getDisplayName());
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}

			// update the current context back to the original user
			SecurityContextHolder.getContext().setAuthentication(originalUser);

			// redirect to target url
			successHandler.onAuthenticationSuccess(request, response,
					originalUser);

			return;
		}

		chain.doFilter(request, response);
	}

	/**
	 * Attempt to switch to another user. If the user does not exist or is not
	 * active, return null.
	 * 
	 * @return The new <code>Authentication</code> request if successfully
	 *         switched to another user, <code>null</code> otherwise.
	 * 
	 * @throws UsernameNotFoundException
	 *             If the target user is not found.
	 * @throws LockedException
	 *             if the account is locked.
	 * @throws DisabledException
	 *             If the target user is disabled.
	 * @throws AccountExpiredException
	 *             If the target user account is expired.
	 * @throws CredentialsExpiredException
	 *             If the target user credentials are expired.
	 */

	protected Authentication attemptSwitchUser(HttpServletRequest request)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken targetUserRequest = null;

		String username = request
				.getParameter(SPRING_SECURITY_SWITCH_USERNAME_KEY);

		if (username == null) {
			username = "";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Attempt to switch to user [" + username + "]");
		}

		UserDetails targetUser = userDetailsService.loadUserByUsername(username);
		
		String loginAs = (String) request.getSession().getAttribute(IApplicationConstants.LOGIN_AS);
		if (loginAs != null && IApplicationConstants.ACTIVE_FLAG.equals(loginAs)) {
			// no check required
		} else {
			userDetailsChecker.check(targetUser);
		}

		// OK, create the switch user token
		targetUserRequest = createSwitchUserToken(request, targetUser);

		if (logger.isDebugEnabled()) {
			logger.debug("Switch User Token [" + targetUserRequest + "]");
		}

		// publish event
		if (this.eventPublisher != null) {
			eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
					SecurityContextHolder.getContext().getAuthentication(),
					targetUser));
		}

		return targetUserRequest;
	}

	/**
	 * Attempt to exit from an already switched user.
	 * 
	 * @param request
	 *            The http servlet request
	 * 
	 * @return The original <code>Authentication</code> object or
	 *         <code>null</code> otherwise.
	 * 
	 * @throws AuthenticationCredentialsNotFoundException
	 *             If no <code>Authentication</code> associated with this
	 *             request.
	 */
	protected Authentication attemptExitUser(HttpServletRequest request)
			throws AuthenticationCredentialsNotFoundException {
		// need to check to see if the current user has a
		// SwitchUserGrantedAuthority
		Authentication current = SecurityContextHolder.getContext()
				.getAuthentication();

		if (null == current) {
			throw new AuthenticationCredentialsNotFoundException(
					messages.getMessage("SwitchUserFilter.noCurrentUser",
							"No current user associated with this request"));
		}

		// check to see if the current user did actual switch to another user
		// if so, get the original source user so we can switch back
		Authentication original = getSourceAuthentication(current);

		if (original == null) {
			logger.error("Could not find original user Authentication object!");
			throw new AuthenticationCredentialsNotFoundException(
					messages.getMessage(
							"SwitchUserFilter.noOriginalAuthentication",
							"Could not find original Authentication object"));
		}

		// get the source user details
		UserDetails originalUser = null;
		Object obj = original.getPrincipal();

		if ((obj != null) && obj instanceof UserDetails) {
			originalUser = (UserDetails) obj;
		}

		// publish event
		if (this.eventPublisher != null) {
			eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
					current, originalUser));
		}

		return original;
	}

	/**
	 * Create a switch user token that contains an additional
	 * <tt>GrantedAuthority</tt> that contains the original
	 * <code>Authentication</code> object.
	 * 
	 * @param request
	 *            The http servlet request.
	 * @param targetUser
	 *            The target user
	 * 
	 * @return The authentication token
	 * 
	 * @see SwitchUserGrantedAuthority
	 */
	private UsernamePasswordAuthenticationToken createSwitchUserToken(
			HttpServletRequest request, UserDetails targetUser) {

		UsernamePasswordAuthenticationToken targetUserRequest;

		// grant an additional authority that contains the original
		// Authentication object
		// which will be used to 'exit' from the current switched user.
		Authentication currentAuth = SecurityContextHolder.getContext()
				.getAuthentication();
		
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) currentAuth.getPrincipal();
		UserTO user = authenticatedUser.getUserTO();
		List<GrantedAuthority> authorities = user.getRoles();
		List<String> roleList = new ArrayList<String>();
		for(GrantedAuthority auth : authorities){
			roleList.add(auth.getAuthority());
		}
		/*Check if the current user has admin role to do loginAS */
		if(roleList.contains("ROLE_ADMIN")){
			GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(
					ROLE_PREVIOUS_ADMINISTRATOR, currentAuth);

			// get the original authorities
			Collection<GrantedAuthority> orig = (Collection<GrantedAuthority>) targetUser
					.getAuthorities();

			// Allow subclasses to change the authorities to be granted
			if (switchUserAuthorityChanger != null) {
				orig = (Collection<GrantedAuthority>) switchUserAuthorityChanger
						.modifyGrantedAuthorities(targetUser, currentAuth, orig);
			}

			// add the new switch user authority
			List<GrantedAuthority> newAuths = new ArrayList<GrantedAuthority>(orig);
			newAuths.add(switchAuthority);

			// create the new authentication token
			targetUserRequest = new UsernamePasswordAuthenticationToken(targetUser,
					targetUser.getPassword(), newAuths);

			// set details
			targetUserRequest.setDetails(authenticationDetailsSource
					.buildDetails(request));
			return targetUserRequest;
		} else {
			return null;
		}
		
	}

	/**
	 * Find the original <code>Authentication</code> object from the current
	 * user's granted authorities. A successfully switched user should have a
	 * <code>SwitchUserGrantedAuthority</code> that contains the original source
	 * user <code>Authentication</code> object.
	 * 
	 * @param current
	 *            The current <code>Authentication</code> object
	 * 
	 * @return The source user <code>Authentication</code> object or
	 *         <code>null</code> otherwise.
	 */
	private Authentication getSourceAuthentication(Authentication current) {
		Authentication original = null;

		// iterate over granted authorities and find the 'switch user' authority
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) current
				.getAuthorities();

		for (GrantedAuthority auth : authorities) {
			// check for switch user type of authority
			if (auth instanceof SwitchUserGrantedAuthority) {
				original = ((SwitchUserGrantedAuthority) auth).getSource();
				logger.debug("Found original switch user granted authority ["
						+ original + "]");
			}
		}

		return original;
	}

	/**
	 * Checks the request URI for the presence of <tt>exitUserUrl</tt>.
	 * 
	 * @param request
	 *            The http servlet request
	 * 
	 * @return <code>true</code> if the request requires a exit user,
	 *         <code>false</code> otherwise.
	 * 
	 * @see SwitchUserFilter#exitUserUrl
	 */
	protected boolean requiresExitUser(HttpServletRequest request) {
		String uri = stripUri(request);

		return uri.endsWith(request.getContextPath() + exitUserUrl);
	}

	/**
	 * Checks the request URI for the presence of <tt>switchUserUrl</tt>.
	 * 
	 * @param request
	 *            The http servlet request
	 * 
	 * @return <code>true</code> if the request requires a switch,
	 *         <code>false</code> otherwise.
	 * 
	 * @see SwitchUserFilter#switchUserUrl
	 */
	protected boolean requiresSwitchUser(HttpServletRequest request) {
		String uri = stripUri(request);

		return uri.endsWith(request.getContextPath() + switchUserUrl);
	}

	public void setApplicationEventPublisher(
			ApplicationEventPublisher eventPublisher) throws BeansException {
		this.eventPublisher = eventPublisher;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource,
				"AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		Assert.notNull(messageSource, "messageSource cannot be null");
		this.messages = new MessageSourceAccessor(messageSource);
	}

	/**
	 * Sets the authentication data access object.
	 * 
	 * @param userDetailsService
	 *            The <tt>UserDetailService</tt> which will be used to load
	 *            information for the user that is being switched to.
	 */
	public void setUserDetailsService(UserDetailsManager userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public UserDetailsManager getUserDetailsService() {
		return this.userDetailsService;
	}

	/**
	 * Set the URL to respond to exit user processing.
	 * 
	 * @param exitUserUrl
	 *            The exit user URL.
	 */
	public void setExitUserUrl(String exitUserUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(exitUserUrl),
				"exitUserUrl cannot be empty and must be a valid redirect URL");
		this.exitUserUrl = exitUserUrl;
	}

	public String getExitUserUrl() {
		return this.exitUserUrl;
	}

	/**
	 * Set the URL to respond to switch user processing.
	 * 
	 * @param switchUserUrl
	 *            The switch user URL.
	 */
	public void setSwitchUserUrl(String switchUserUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(switchUserUrl),
				"switchUserUrl cannot be empty and must be a valid redirect URL");
		this.switchUserUrl = switchUserUrl;
	}

	public String getSwitchUserUrl() {
		return this.switchUserUrl;
	}

	/**
	 * Sets the URL to go to after a successful switch / exit user request. Use
	 * {@link #setSuccessHandler(AuthenticationSuccessHandler)
	 * setSuccessHandler} instead if you need more customized behaviour.
	 * 
	 * @param targetUrl
	 *            The target url.
	 */
	public void setTargetUrl(String targetUrl) {
		logger.debug("********************************************************** TARGET URL *********** " + targetUrl);
		this.targetUrl = targetUrl;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	/**
	 * Used to define custom behaviour on a successful switch or exit user.
	 * <p>
	 * Can be used instead of setting <tt>targetUrl</tt>.
	 */
	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	/**
	 * Sets the URL to which a user should be redirected if the switch fails.
	 * For example, this might happen because the account they are attempting to
	 * switch to is invalid (the user doesn't exist, account is locked etc).
	 * <p>
	 * If not set, an error message will be written to the response.
	 * <p>
	 * Use {@link #setFailureHandler(AuthenticationFailureHandler)
	 * failureHandler} instead if you need more customized behaviour.
	 * 
	 * @param switchFailureUrl
	 *            the url to redirect to.
	 */
	public void setSwitchFailureUrl(String switchFailureUrl) {
		Assert.isTrue(
				StringUtils.hasText(switchUserUrl)
						&& UrlUtils.isValidRedirectUrl(switchFailureUrl),
				"switchFailureUrl cannot be empty and must be a valid redirect URL");
		this.switchFailureUrl = switchFailureUrl;
	}

	/**
	 * Used to define custom behaviour when a switch fails.
	 * <p>
	 * Can be used instead of setting <tt>switchFailureUrl</tt>.
	 */
	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	/**
	 * @param switchUserAuthorityChanger
	 *            to use to fine-tune the authorities granted to subclasses (may
	 *            be null if SwitchUserFilter should not fine-tune the
	 *            authorities)
	 */
	public void setSwitchUserAuthorityChanger(
			SwitchUserAuthorityChanger switchUserAuthorityChanger) {
		this.switchUserAuthorityChanger = switchUserAuthorityChanger;
	}

	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	/**
	 * Strips any content after the ';' in the request URI
	 * 
	 * @param request
	 *            The http request
	 * 
	 * @return The stripped uri
	 */
	private String stripUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		int idx = uri.indexOf(';');

		if (idx > 0) {
			uri = uri.substring(0, idx);
		}

		return uri;
	}
}
