package com.ctb.prism.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.jms.JmsMessageProducer;

@Component
public final class CacheKeyUtils {
	
	private static final Logger LOG = Logger.getLogger(JmsMessageProducer.class);
    
	@Autowired private JmsMessageProducer autoMessageProducer;
	
	private static JmsMessageProducer messageProducer;
	
	private static String contractName;
	
	@PostConstruct
    public void init() {
		CacheKeyUtils.messageProducer = autoMessageProducer;
    }
	
    /*private CacheKeyUtils() {
        throw new UnsupportedOperationException();
    }*/

    public static <K extends Comparable<K>> String key(Collection<K> col) {
        if (col == null) {
            return "";
        }

        final List<K> sorted = new ArrayList<K>(col);

        if (col.size() > 1) {
            Collections.sort(sorted);
        }

        final StringBuilder b = new StringBuilder("[");
        for (K entry : sorted) {
            if (entry != null) {
                b.append(entry);
                b.append(",");
            }
        }
        b.append("]");
        return b.toString();
    }
    
    public static String listKey(List<String> list) {
    	if(list != null) {
    		if(list.size() > 1) Collections.sort(list);
    		StringBuilder sb = new StringBuilder();
    		for(String str : list) {
    			sb.append(str);
    		}
    		return sb.toString();
    	} else {
    		return "";
    	}
    }

    public static <K extends Comparable<K>> String mapKey(Map<K, ?> col) {
        
    	//String contractName = null; 
        
    	if (col == null) {
            return "";
        }

        final List<K> sorted = new ArrayList<K>(col.keySet());
    
        if (col.size() > 1) {
            Collections.sort(sorted);
        }

        final StringBuilder b = new StringBuilder("[");
        for (K entry : sorted) {
            if (entry != null 
            		&& !"REPORT_CONTEXT".equals(entry) 
            		&& !"net.sf.jasperreports.parameter.jasperdesign.cache".equals(entry)
            		&& !"net.sf.jasperreports.data.cache.handler".equals(entry)) {
                b.append(entry);
                b.append("|");
                b.append(col.get(entry));
                b.append(",");
            }
        }
        b.append("]");
        
        if(col.get("contractName")!=null && ((String)col.get("contractName")).length() > 0) {
        	contractName = (String)col.get("contractName");
        } else {
        	contractName = Utils.getContractName();
        }
        
        return b.toString();
    }
    
    public static String string(String col) {
		if(col != null) return col.replaceAll(" ", "_");
    	return "";
    }
    
    public static String string(boolean col) {
    	return col+"";
    }
    
    public static String encryptedKey(String col) {
    	String hashKey = "";
    	if(col != null) {
    		hashKey = SaltedPasswordEncoder.encryptPassword(col + Utils.getContractName(), null, 1);
    		// store the key into queue
    		storeCacheKey(hashKey);
    	}
    	return hashKey;
    }
    
    public static String generateKey(Object... param) {
    	StringBuffer buf = new StringBuffer();
		for (Object n : param) {
			if(n != null) buf.append(String.valueOf(n));
		}
		return encryptedKey(buf.toString());
    }
    
    /*
     * Store cache key in SQS for clearing based on contract
     * creates message and push into amazon SQS
     * @param key
     * @param contractName
     * 
     */
    private static void storeCacheKey(String key){
    	if(messageProducer != null) messageProducer.putCacheKey(key, contractName);
    	else LOG.error("Unable to store key into Queue. MessageProducer is null.");
    }
    
    
    public static void main(String[] args) {
		System.out.println( generateKey("abc", 123, true, null) );
	}
    
}
