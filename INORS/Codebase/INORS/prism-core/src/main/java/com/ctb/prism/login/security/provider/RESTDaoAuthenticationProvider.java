package com.ctb.prism.login.security.provider;

import groovy.util.logging.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class RESTDaoAuthenticationProvider extends DaoAuthenticationProvider {


    private PasswordEncoder passwordEncoder;
    
   
    /**
	* This is the method which actually performs the check to see whether the user is indeed the correct user
	* @param userDetails
	* @param authentication
	* @throws AuthenticationException
	*/
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException { // here we check if the details provided by the user actually stack up.
    	AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetails;
    	
		if (!passwordEncoder.isPasswordValid(authenticatedUser.getPassword(), (String) authentication.getCredentials(), authenticatedUser.getSalt())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    	
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
}
