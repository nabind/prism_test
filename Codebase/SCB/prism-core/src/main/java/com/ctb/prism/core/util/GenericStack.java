package com.ctb.prism.core.util;
import java.util.ArrayList;
/**
 * This class is used to implement stack using ArrayList
 * extend this class
 *
 * @author Joy
 * @version 1.0
 */


public class GenericStack<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

	public void push(T o) {
        add(o);
    }

    public T pop() {
        return remove(size() - 1);
    }
    
    public boolean isEmpty(){
         return (size() == 0);
    }
}

