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
            if (entry != null && !"REPORT_CONTEXT".equals(entry) && !"net.sf.jasperreports.parameter.jasperdesign.cache".equals(entry)
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
    
    public static String encryptedKey(String col) {
    	if(col != null) return SaltedPasswordEncoder.encryptPassword(col, null);
    	return "";
    }
    
}
