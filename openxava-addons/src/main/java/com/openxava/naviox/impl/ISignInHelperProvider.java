package com.openxava.naviox.impl;

import javax.servlet.*;
import javax.servlet.http.*;

import org.openxava.util.*;
import org.openxava.view.*;

/**
 * tmr �Documentar? Yo dir�a que s�, porque es una nueva caracter�sticas para v7, creo que pedida
 * 
 * @since 7.0
 * @author Javier Paniza
 */

public interface ISignInHelperProvider {
	
	void init(HttpServletRequest request, View view);
	
	String refineForwardURI(HttpServletRequest request, String forwardURI);
	
	void signIn(HttpServletRequest request, String userName);
	
	boolean isAuthorized(ServletRequest request, String userName, String password, Messages errors, String unauthorizedMessage);
	
}
