package com.ctb.prism.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class CacheKeyUtils {
    
    private CacheKeyUtils() {
        throw new UnsupportedOperationException();
    }

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
    	if(col != null) return SaltedPasswordEncoder.encryptPassword(col, null, 1);
    	return "";
    }
    
    public static String generateKey(Object... param) {
    	StringBuffer buf = new StringBuffer();
		for (Object n : param) {
			if(n != null) buf.append(String.valueOf(n));
		}
		return encryptedKey(buf.toString());
    }
    
    public static void main(String[] args) {
		System.out.println( generateKey("abc", 123, true, null) );
	}
    
}
