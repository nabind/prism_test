
package com.ctb.prism.login.security.filter;

import groovy.util.logging.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.security.encoder.DigitalMeasuresHMACQueryStringBuilder;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;

@Slf4j
public class RESTAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {

	private static final String DUMMY_SSO_USERNAME = "dummyssouser";
	private static final String DUMMY_SSO_PASSWORD = "Passwd12";
	private static final String ADMIN = "Admin";

    private static final String API_KEY_PARAMETER_NAME = "apikey";
    private static final String EXPIRY_DATE_PARAMETER_NAME = "validUntil";
    private static final String IP_ADDRESS_PARAMETER_NAME = "ipAddress";
    private static final String SIGNATURE_PARAMETER_NAME = "signature";
    private static final String CUSTOMER_ID_PARAM = "customer_id";
	private static final String ORG_NODE_PARAM = "org_node_code";
	private static final String HIERARCHY_LAVEL_PARAM = "hierarchy_level";
	private static final String APPLICATION_NAME_PARAM = "application_name";
    private static final String EXPIRY_DATE_PARAM = "time_stamp";
    private static final String USER_ROLE_PARAM = "user_role";
    private static final String USER_NAME_PARAM = "user_name";
    
    private static final String THEME_PARAM = "project";
    
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "j_password";
    public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    
    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
    private boolean continueChainBeforeSuccessfulAuthentication = false;
    private List<String> alternateAuthenticationUrls;
    private boolean isWebServiceCall = false;
    private boolean postOnly = true;
    
    public static String RANDOM_STRING = "9rc^wH7KRg[B";
    String RANDOM_PASSWD = "8rc^wK6HRg[C";
    
	@Autowired
    DigitalMeasuresHMACQueryStringBuilder hmac;
    
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private CookieThemeResolver themeResolver;
	
	/**
	 * @param defaultFilterProcessesUrl
	 *            the default value for <tt>filterProcessesUrl</tt>.
	 */
    protected RESTAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String apiKeyValue = null;
        String expiryTime = null;
        String ipAddress = null;
        String signature = null;
        String customerId = null;
        String orgNode = null;
        String orgLevel = null;
        String applicationName = null;
        String role = null;
        String userName = null;
        /* ## new for eR candidate report */
        String studentId = null;
        String uuid = null;
        /* ## end new for eR candidate report */
        
        //Added for single Codebase
        String theme = null;
        
        /*String customerId, String orgNode, 
			String orgLevel, String applicationName, String role, 
			String validUntilDate, String secretValue*/
        
        try {
        	if(IApplicationConstants.SOAP.equals(request.getHeader(IApplicationConstants.CLIENT_TYPE))) isWebServiceCall = true;
        	else if (IApplicationConstants.REST.equalsIgnoreCase(request.getParameter(IApplicationConstants.CLIENT_TYPE))) isWebServiceCall = true;
        	else isWebServiceCall = false;
        	apiKeyValue = obtainAPIKeyValue(request);
        	expiryTime = getHeaderValue(request, EXPIRY_DATE_PARAM); //obtainExpiryValue(request);
        	ipAddress = obtainIPValue(request);
        	signature = obtainSignatureValue(request);
        	customerId = getHeaderValue(request, CUSTOMER_ID_PARAM);
            orgNode = getHeaderValue(request, ORG_NODE_PARAM);
            orgLevel = getHeaderValue(request, HIERARCHY_LAVEL_PARAM);
            applicationName = getHeaderValue(request, APPLICATION_NAME_PARAM);
            role = getHeaderValue(request, USER_ROLE_PARAM);
            userName = getHeaderValue(request, USER_NAME_PARAM);
            /* ## new for eR candidate report */
            studentId = getHeaderValue(request, "ctbstudentid");
            uuid = getHeaderValue(request, "uuid");
            /* ## end new for eR candidate report */
            theme = getHeaderValue(request, THEME_PARAM);
        } catch (Exception ex) {ex.printStackTrace();}
        
        logger.info("customerId : {}" + customerId);
        logger.info("expiryTime : {}" + expiryTime);
        logger.info("orgNode : {}" + orgNode);
        logger.info("orgLevel : {}" + orgLevel);
        logger.info("applicationName : {}" + applicationName);
        logger.info("role : {}" + role);
        logger.info("userName : {}" + userName);
        logger.info("signature : {}" + signature);
        logger.info("theme : {}" + theme);
        if(theme != null && theme.trim().length() == 0) {
        	// this section is to support OLD applications
        	if("OAS".equalsIgnoreCase(applicationName)) theme = "tasc";
        	if("CTB.COM".equalsIgnoreCase(applicationName)) theme = "inors";
        }
        response.addCookie(new Cookie("SSOAPP", applicationName));

        if(!"".equals(applicationName)) {
	        System.out.println("customerId : {} " + customerId);
	        System.out.println("expiryTime : {} " + expiryTime);
	        System.out.println("orgNode : {} " + orgNode);
	        System.out.println("orgLevel : {} " + orgLevel);
	        System.out.println("applicationName : {} " + applicationName);
	        System.out.println("role : {} " + role);
	        System.out.println("userName : {} " + userName);
	        System.out.println("signature : {} " + signature);
	        System.out.println("theme : {} " + theme);
        }
        
        if(signature != null && !signature.isEmpty()) { // SSO request
        	logger.info("Authentication Filter : Validating request type.");
        	try {
        		/* ## new for eR candidate report */
        		if(studentId != null && studentId.length() > 0) {
        			// request for candidate report download
        			if(hmac.isValidRequest(applicationName, expiryTime, studentId, signature, uuid, orgNode, theme)) {
	        			// Authenticate user
	    	        	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(DUMMY_SSO_USERNAME, DUMMY_SSO_PASSWORD);
	    	        	authRequest.setContractName(Utils.getContractNameNoLogin(theme));
			        	themeResolver.setThemeName(request, response, theme);
	    	        	// Allow subclasses to set the "details" property
	    		        setDetails(request, authRequest);
	    		        return this.getAuthenticationManager().authenticate(authRequest);
        			} else {
						logger.error("Security token is not valid or parameter tampered");
						throw new AuthenticationServiceException("Security token is not valid or parameter tampered");
					}
        		} else
        		/* ## end new for eR candidate report */
				if(hmac.isValidRequest(customerId, orgNode, orgLevel, applicationName, role, userName, expiryTime, signature, theme)) {
					logger.info("Authentication Filter : User request is valid, authenticating with dummy user.");
					//System.out.println("Authentication Filter : User request is valid, authenticating with dummy user.");
					// TODO -- here we need to change the logic based on i/p parameters
					//UserTO userTO =loginService.getUsersForSSO(apiKeyValue);
					/**
					 *  dummyssouser user will have role ROLE_SSO and ROLE_USER in system and will have a default password
					 *  based on admin flag from request ROLE_ADMIN will be added by system
					 *  This user will not be associated with any ORG
					 */														
					UserTO userTO = new UserTO();
					userTO.setCustomerId(customerId);
					userTO.setOrgNodeLevelStr(orgLevel);
					userTO.setOrgCode(CustomStringUtil.appendString("0~", URLDecoder.decode(orgNode, "UTF-8")));
					
					userTO.setContractName(Utils.getContractNameNoLogin(theme));
					
					// get prism customer id and orgnode id
					userTO = loginService.getOrgLevel(userTO);
					if(userTO != null) {
						// customer id org level and orgnode id is valid - which are received from request
						//System.out.println("customer id org level and orgnode id is valid - which are received from request.");
						// create sso user in prism
						String ssoUsername = CustomStringUtil.appendString(userName, orgLevel, userTO.getCustomerId(), RANDOM_STRING);
						ssoUsername = (ssoUsername.length() > 30)? ssoUsername.substring(0, 30) : ssoUsername; // max length is 30 char in prism
						
						String existingUserOrg = loginService.getUserOrgNode(ssoUsername,Utils.getContractNameNoLogin(theme));
						if(existingUserOrg == null || "".equals(existingUserOrg)) {
						//if(loginService.checkUserAvailability(ssoUsername)) {
							// if user  not present into system
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("userName", ssoUsername);
							paramMap.put("password", RANDOM_PASSWD);
							paramMap.put("userDisplayName", (userName.length() > 10 )? userName.subSequence(0, 10) : userName);
							paramMap.put("userStatus", IApplicationConstants.SS_FLAG);
							paramMap.put("customer", userTO.getCustomerId());
							paramMap.put("tenantId", userTO.getOrgId());
							paramMap.put("orgLevel", orgLevel);
							if(ADMIN.equalsIgnoreCase(role)){
								paramMap.put("userRoles", new String[]{"ROLE_USER","ROLE_ADMIN"});
							}else{
								paramMap.put("userRoles", new String[]{"ROLE_USER"});
							}
							paramMap.put("contractName", Utils.getContractNameNoLogin(theme));
							loginService.addNewUser(paramMap);
							logger.info("SSO user is created : " + ssoUsername);
							//System.out.println("SSO user is created : " + ssoUsername);
						}else {
							// user exists into system
							//System.out.println("SSO user exists into system " + ssoUsername);
							/** handle user move to different school */
							if(userTO.getOrgId() != null) {
								if(!existingUserOrg.equals(userTO.getOrgId())) {
									// user org is changed from last login - update org_nodeid
									logger.info("Updating user Org_nodeid as seems it moved to differnt school/org");
									loginService.updateUserOrg(ssoUsername, userTO.getOrgId(), existingUserOrg, Utils.getContractNameNoLogin(theme));
								}
							}
							
							/**
							 * Check for user role
							 */
							Map<String,Object> paramMapForUserRole = new HashMap<String,Object>();
							paramMapForUserRole.put("username",ssoUsername);
							paramMapForUserRole.put("contractName",Utils.getContractNameNoLogin(theme));
							if(ADMIN.equalsIgnoreCase(role)){
								paramMapForUserRole.put("userRole", "ROLE_ADMIN");
							}else{
								paramMapForUserRole.put("userRole", "ROLE_USER");
							}
							logger.info("Updating user role as seems it assigned to differnt role");
							loginService.checkUserRoleByUsername(paramMapForUserRole);
						}
						
						userTO.setUserName(ssoUsername);
						request.getSession().setAttribute(IApplicationConstants.SSO_ORG, userTO.getOrgId());
						request.getSession().setAttribute(IApplicationConstants.SSO_ORG_LEVEL, userTO.getOrgNodeLevel());
						request.getSession().setAttribute(IApplicationConstants.SSO_ADMIN, ADMIN.equalsIgnoreCase(role));
						
						// Authenticate user
			        	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userTO.getUserName(), RANDOM_PASSWD);
						// added to pass contract name in custom UsernamePasswordAuthenticationToken
			        	authRequest.setContractName(Utils.getContractNameNoLogin(theme));
			        	themeResolver.setThemeName(request, response, theme);
			        	
			        	//AbstractAuthenticationToken authRequest = createAuthenticationToken(apiKeyValue, new RESTCredentials("ctbadminJkmqbrbaccesfejrtay9","MkiG+l/qCHbbAlbvAuk6QAWkR68WZAPVNIsxBj8G6P0="));
				
				        // Allow subclasses to set the "details" property
				        setDetails(request, authRequest);
				
				        return this.getAuthenticationManager().authenticate(authRequest);
					} else {
						logger.error("Organization code, org level or customer id received are not valid/matching with PRISM system.");
						throw new AuthenticationServiceException("Organization code, org level or customer id received are not valid/matching with PRISM system.");
					}
				} else {
					logger.error("Authentication Filter : User request is not valid. Either token is expired or request parameters are tempared.");
					throw new AuthenticationServiceException("Authentication Filter : User request is not valid. Either token is expired or request parameters are tempared.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new AuthenticationServiceException("User request is not valid. Either token is expired or request parameters are tempared." + e.getMessage());
			}
        	
        } else {
        	String queryStr = request.getQueryString();
        	if(queryStr != null && (queryStr.endsWith("wsdl") || queryStr.startsWith("xsd"))) {
        		// WSDL request - we need to login as a dummy user
        		isWebServiceCall = true;
        		
        		// Authenticate user
	        	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(DUMMY_SSO_USERNAME, DUMMY_SSO_PASSWORD);
				// added to pass contract name in custom UsernamePasswordAuthenticationToken
	        	String contract = request.getParameter("j_contract");
	        	if(contract != null && contract.trim().length() > 0) {
	        		
	        	} else {
	        		contract = "inors"; // fallback
	        	}
	        	authRequest.setContractName(contract);
		        // Allow subclasses to set the "details" property
		        setDetails(request, authRequest);
		
		        return this.getAuthenticationManager().authenticate(authRequest);
        	} else {
	        	//return super.attemptAuthentication(request, response);
	        	if (postOnly && !request.getMethod().equals("POST")) {
	                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
	            }
	
	            String username = obtainUsername(request);
	            String password = obtainPassword(request);
	
	            if (username == null) {
	                username = "";
	            }
	
	            if (password == null) {
	                password = "";
	            }
	
	            username = username.trim();
	
	            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
	            // added to pass contract name in custom UsernamePasswordAuthenticationToken
	            // Blocked as j_contract was hardcoded in jsp
	        	// authRequest.setContractName(request.getParameter("j_contract"));
	            String contractName = Utils.getContractNameNoLogin(themeResolver.resolveThemeName(request));
	            logger.info("RESTAuthenticationFilter.contractName = " + contractName);
	            authRequest.setContractName(contractName);
	            // Allow subclasses to set the "details" property
	            setDetails(request, authRequest);
	
	            return this.getAuthenticationManager().authenticate(authRequest);
        	}
        }
    }
    
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }


    private String obtainIPValue(HttpServletRequest request) throws UnsupportedEncodingException {

        return getHeaderValue(request, IP_ADDRESS_PARAMETER_NAME);
    }
    
    private String obtainSignatureValue(HttpServletRequest request) throws UnsupportedEncodingException {

        return getHeaderValue(request, SIGNATURE_PARAMETER_NAME);
    }

    private String decodeParameterValue(HttpServletRequest request, String requestParameterName) throws UnsupportedEncodingException {
        //This is basically to avoid the weird RFC spec when it comes to spaces in the URL and how they are encoded
        /*
        return URLDecoder.decode(getParameterValue(request, requestParameterName), request.getCharacterEncoding())
                .replaceAll(" ", "+");
        */
        return getParameterValue(request, requestParameterName);
    }
    
    private String getHeaderValue(HttpServletRequest request, String headerParameterName) {
    	return (request.getHeader(headerParameterName) != null) ? 
        		request.getHeader(headerParameterName) : getParameterValue(request, headerParameterName);
    }
    
    private String getParameterValue(HttpServletRequest request, String requestParameterName) {
        return (request.getParameter(requestParameterName) != null) ? request.getParameter(requestParameterName) : "";
    }
    
    private String obtainExpiryValue(HttpServletRequest request) throws UnsupportedEncodingException {

        return getHeaderValue(request, EXPIRY_DATE_PARAMETER_NAME);
    }

    private String obtainAPIKeyValue(HttpServletRequest request) throws UnsupportedEncodingException {
        return getHeaderValue(request, API_KEY_PARAMETER_NAME);
    }

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 * 
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /*private AbstractAuthenticationToken createAuthenticationToken(String apiKeyValue, RESTCredentials restCredentials) {
        return new RESTAuthenticationToken(apiKeyValue,restCredentials);
    }*/

    //@Override
    /**
	* Because we require the API client to send credentials with every request, we must authenticate on every request
	*/
    /*protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }*/
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String queryStr = request.getQueryString();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        if ("".equals(request.getContextPath())) {
            if(uri.endsWith(super.getFilterProcessesUrl())) return true;
            //else if(queryStr != null && queryStr.endsWith("wsdl")) return false;
            else return isAuthenticationNeeded(uri);
        }

        if(uri.endsWith(request.getContextPath() + super.getFilterProcessesUrl())) return true;
        //else if(queryStr != null && queryStr.endsWith("wsdl")) return false;
        else return isAuthenticationNeeded(uri);
    }
    
	/**
	 * Check if the requested uri exists in user provided list
	 * 
	 * @param uri
	 * @return <code>true</code> if AlternateAuthenticationUrls contains the uri
	 *         else returns <code>false</code>
	 */
    private boolean isAuthenticationNeeded(String uri) {
    	for(String url : getAlternateAuthenticationUrls()) {
    		if(uri.endsWith(url)) return true;
    	}
    	return false;
    }
    
    @SuppressWarnings("deprecation")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (!requiresAuthentication(request, response)) {
			chain.doFilter(request, response);

			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Request is to process authentication");
		}

		Authentication authResult;

		try {
			authResult = attemptAuthentication(request, response);
			if (authResult == null) {
				// return immediately as subclass has indicated that it hasn't
				// completed authentication
				return;
			}
			sessionStrategy.onAuthentication(authResult, request, response);
		} catch (AuthenticationException failed) {
			// Authentication failed
			logger.error("Authentication failed :: " + failed.getMessage());
			unsuccessfulAuthentication(request, response, failed);

			return;
		}

		// Authentication success
		if (continueChainBeforeSuccessfulAuthentication) {
			chain.doFilter(request, response);
		}

		successfulAuthentication(request, response, authResult);
		
		// if webservice call - then continue with current request
		if(isWebServiceCall) chain.doFilter(request, response);
	}
    
    /*protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        rememberMeServices.loginSuccess(request, response, authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        successHandler.onAuthenticationSuccess(request, response, authResult);
    }*/
    
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

    
    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }
    
    public void setContinueChainBeforeSuccessfulAuthentication(boolean continueChainBeforeSuccessfulAuthentication) {
        this.continueChainBeforeSuccessfulAuthentication = continueChainBeforeSuccessfulAuthentication;
    }
    
    public List<String> getAlternateAuthenticationUrls() {
		return alternateAuthenticationUrls;
	}

	public void setAlternateAuthenticationUrls(
			List<String> alternateAuthenticationUrls) {
		this.alternateAuthenticationUrls = alternateAuthenticationUrls;
	}

}
