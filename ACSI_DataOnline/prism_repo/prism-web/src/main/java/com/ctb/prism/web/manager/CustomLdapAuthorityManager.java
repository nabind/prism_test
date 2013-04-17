package com.ctb.prism.web.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import com.ctb.prism.login.Service.ILoginService;

public class CustomLdapAuthorityManager implements LdapAuthoritiesPopulator {

	@Autowired
	private ILoginService loginService;
	
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(
			DirContextOperations userData, String username) {
		/*ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));*/
		
		return loginService.getGrantedAuthorities(username);
	}
}