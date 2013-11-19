package com.ctb.prism.login.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

public class CustomGrantedAuthority implements GrantedAuthority  {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String role;
    private String firstTimeLogin;

    public CustomGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }
    
    public CustomGrantedAuthority(String role, String firstTimeLogin) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
        this.firstTimeLogin = firstTimeLogin;
    }

    public String getAuthority() {
        return role;
    }
    
    public String getFirstTimeLogin() {
        return firstTimeLogin;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof CustomGrantedAuthority) {
            return role.equals(((CustomGrantedAuthority) obj).role);
        }

        return false;
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }
	
}
