package org.openxava.web.servlets;

import java.io.*;
import java.nio.charset.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.commons.io.*;
import org.apache.commons.logging.*;
import org.openxava.controller.*;
import org.openxava.util.*;

/**
 * @author Chungyen Tsai
 */

@WebServlet("/xava/style/*")
public class CSSServlet extends HttpServlet {
	
	private static Log log = LogFactory.getLog(CSSServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getRequestURI().endsWith(".css")) {
				String path = request.getServletPath() + request.getPathInfo();
				System.out.println(request.getServletPath());
				System.out.println(request.getPathInfo());
				//InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/resources/" + path);
				InputStream inputStream = Resources.getAsStreamInPrefixes(request.getPathInfo().toString(), request.getServletPath().toString());
				//URL url = Resources.getAsStreamInPrefixes(path, "META-INF/resources/", "../webapp/", "/");
				
				
				System.out.println(inputStream.getClass().getResource(path).getPath());
				StringWriter writer = new StringWriter();
				IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
				String data = writer.toString().replaceAll("@import (['\"].*)\\.css", "@import $1.css?ox=" + ModuleManager.getVersion());
				response.getWriter().append(data);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServletException(XavaResources.getString("attachments_css_servlet_error", request.getServletPath() + request.getPathInfo()));
		}
	}

}
