package com.ctb.prism.login.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

public class NoRedirectStrategy implements RedirectStrategy{

    
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        // Forget about redirecting, there is no need!
    	//response.sendRedirect("validateUser.do");
    }
}
