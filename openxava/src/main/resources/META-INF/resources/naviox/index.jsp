<%Servlets.setCharacterEncoding(request, response);%>

<%--
NaviOX. Navigation and security for OpenXava applications.
Copyright 2014 Javier Paniza Lucas

License: Apache License, Version 2.0.	
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

License: GNU Lesser General Public License (LGPL), version 2.1 or later.
See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
--%>

<%@ include file="../xava/imports.jsp"%>

<%@ page import="com.openxava.naviox.util.NaviOXPreferences"%>
<%@ page import="org.openxava.web.servlets.Servlets"%>
<%@ page import="org.openxava.util.Locales"%>
<%@ page import="org.openxava.util.XavaPreferences"%>
<%@ page import="org.openxava.web.style.XavaStyle"%>
<%@ page import="org.openxava.web.style.Themes"%> 
<%@ page import="org.apache.commons.logging.LogFactory" %> 
<%@ page import="org.apache.commons.logging.Log" %> 

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="modules" class="com.openxava.naviox.Modules" scope="session"/>

<%!
private static Log log = LogFactory.getLog("index.jsp"); 
%>

<%
String windowId = context.getWindowId(request);
context.setCurrentWindowId(windowId);
if ("true".equals(request.getParameter("init"))) { 										
	context.resetModule(request);
}
String app = request.getParameter("application");
String module = context.getCurrentModule(request);
try {
	modules.setCurrent(request);
}
catch (org.openxava.util.ElementNotFoundException ex) {
	log.error(ex.getMessage(), ex);
	response.sendError(HttpServletResponse.SC_NOT_FOUND);
	return;
}
String oxVersion = org.openxava.controller.ModuleManager.getVersion();
String title = (String) request.getAttribute("naviox.pageTitle");
if (title == null) title = modules.getCurrentModuleDescription(request); 
boolean hasModules = modules.hasModules(request); 		
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context
		.get(app, module, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
manager.setApplicationName(request.getParameter("application"));
manager.setModuleName(module); // In order to show the correct description in head
%>

<!DOCTYPE html>

<head>
	<title><%=title%></title>
	<link href="<%=request.getContextPath()%>/xava/style/layout.css?ox=<%=oxVersion%>" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/xava/style/<%=Themes.getCSS(request)%>?ox=<%=oxVersion%>" rel="stylesheet" type="text/css"> 
	<link rel="stylesheet" href="<%=request.getContextPath()%>/xava/style/materialdesignicons.css?ox=<%=oxVersion%>">
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/dwr-engine.js?ox=<%=oxVersion%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Modules.js?ox=<%=oxVersion%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Folders.js?ox=<%=oxVersion%>'></script>
</head>

<body <%=XavaStyle.getBodyClass(request)%>>
	
	<div id="main"> 
				
		<% if (hasModules) { %>
			<jsp:include page="leftMenu.jsp"/>
		<% } %>
		
		<div class="module-wrapper">
			<div id="module_header">
				 <jsp:include page="moduleHeader.jsp"/>
			</div>
			<% if ("SignIn".equals(module)) {  %>
			<jsp:include page='<%=NaviOXPreferences.getInstance().getSignInJSP()%>'/>
			<% } else { %>
			<div id="module"> 	
				<jsp:include page='<%="../xava/module.jsp?application=" + app + "&module=" + module + "&htmlHead=false"%>'/>
			</div> 
			<% } %>
		</div>
		
	</div> 
	
	<%@include file="indexExt.jsp"%>

	<script type='text/javascript' src='<%=request.getContextPath()%>/naviox/js/naviox.js?ox=<%=oxVersion%>'></script> 
	
	<script <xava:nonce/>> 
	$(function() {
		naviox.lockSessionMilliseconds = <%=com.openxava.naviox.model.Configuration.getInstance().getLockSessionMilliseconds()%>; 
		naviox.application = "<%=app%>";
		naviox.module = "<%=module%>";
		naviox.locked = <%=context.get(request, "naviox_locked")%>;
		naviox.init();
	});
	</script>
	

</body>
</html>
