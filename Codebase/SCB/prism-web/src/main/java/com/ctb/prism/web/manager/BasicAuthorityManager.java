package com.ctb.prism.web.manager;
import org.springframework.security.core.GrantedAuthority;


import com.ctb.prism.login.transferobject.UserTO;

@SuppressWarnings("serial")
public class BasicAuthorityManager implements GrantedAuthority {
	
	private UserTO user;
	
	public BasicAuthorityManager(UserTO user) {
		this.user = user;
	}
	
	public String getAuthority() {
		return ((user.getIsAdminFlag()!=null && 
				user.getIsAdminFlag().equalsIgnoreCase("Y"))? "ROLE_ADMIN" : "ROLE_MEMBER");
	}

	public UserTO getUserTO() {
		return user;
	}

	public void setUserTO(UserTO user) {
		this.user = user;
	}
}