package com.ctb.prism.login.security.provider;

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
		/*Collection<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
		authority.add(new BasicAuthorityManager(user));
		return authority;*/
		return user.getRoles();
	}
	
	public String getSalt() {
		return user.getSalt();
	}

	public String getPassword() {		
		return user.getPassword();
	}

	public String getUsername() {		
		return user.getUserName();
	}

	public boolean isAccountNonExpired() {
		return user.isActive();
	}

	public boolean isAccountNonLocked() {		
		return true;
	}

	public boolean isCredentialsNonExpired() {		
		return true;
	}

	public boolean isEnabled() {
		return user.isActive();
	}

	public UserTO getUserTO() {
		return user;
	}
	
	public void setUserTO(UserTO user) {
		this.user = user;
	}

}
