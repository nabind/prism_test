package com.ctb.prism.core.util;

public class Functions {
	public static boolean isNumeric(String number) {  
        try{  
          double d = Double.parseDouble(number);  
        }  
        catch(NumberFormatException nfe){  
          return false;  
        }  
        return true;
	}
}
