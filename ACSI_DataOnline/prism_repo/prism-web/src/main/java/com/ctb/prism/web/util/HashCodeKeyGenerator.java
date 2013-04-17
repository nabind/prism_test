package com.ctb.prism.web.util;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;

public class HashCodeKeyGenerator implements CacheKeyGenerator<Serializable> { 

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
    public Serializable generateKey(MethodInvocation methodInvocation) {
    	return this.generateKey((Object)methodInvocation);
    }

	public Serializable generateKey(Object... arg0) {
		final Date now = new Date();
        
        synchronized (this.dateFormat) {
            return dateFormat.format(now);
        }
	}

	

}
