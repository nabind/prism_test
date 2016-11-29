package com.ctb.prism.web.manager;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ctb.prism.login.transferobject.UserTO;



@SuppressWarnings("serial")
public class AuthenticatedUser implements UserDetails {

	private UserTO user;
	
	public AuthenticatedUser(UserTO user) {
		this.user = user;
	}	
	
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
		authority.add(new BasicAuthorityManager(user));
		return authority;
	}

	public String getPassword() {		
		return user.getPassword();
	}

	public String getUsername() {		
		return user.getUserEmail();
	}

	public boolean isAccountNonExpired() {
		return (("active".equals(user.getUserStatus()))? true:false);
	}

	public boolean isAccountNonLocked() {		
		return true;
	}

	public boolean isCredentialsNonExpired() {		
		return true;
	}

	public boolean isEnabled() {
		return (("active".equals(user.getUserStatus()))? true:false);
	}

	public UserTO getUserTO() {
		return user;
	}
	
	public void setUserTO(UserTO user) {
		this.user = user;
	}

}
