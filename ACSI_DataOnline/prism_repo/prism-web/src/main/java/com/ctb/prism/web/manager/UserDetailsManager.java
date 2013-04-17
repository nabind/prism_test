package com.ctb.prism.web.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.Service.ILoginService;
import com.ctb.prism.login.transferobject.UserTO;



@Component("userDetailsService")
public class UserDetailsManager implements UserDetailsService {
	
	private static final IAppLogger logger = LogFactory.
								getLoggerInstance(UserDetailsManager.class.getName());

	
	@Autowired
	private ILoginService loginService;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		logger.log(IAppLogger.INFO, "Enter: UserDetailsManager - loadUserByUsername");
		try {
			logger.log(IAppLogger.INFO, "EMAIL ::::::::::::::: "+username);
			UserTO user = loginService.getUserByEmail(username);
			UserDetails authenticatedUser = null;
			if(user!=null)
				authenticatedUser = new AuthenticatedUser(user);
			return authenticatedUser;		
		} catch (SystemException systemException) {			
			logger.log(IAppLogger.ERROR, systemException.getMessage());
			return null;
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage());
			return null;
		} finally {
			logger.log(IAppLogger.INFO, "Exit: UserDetailsManager - loadUserByUsername");
		}
	}
}