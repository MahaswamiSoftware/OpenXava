<%-- tmr Mover a openxava --%>

<%
String propertyKey = request.getParameter("propertyKey");
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String prefix = request.getParameter("prefix");
String suffix = request.getParameter("suffix"); 
// TMR ME QUED� POR AQU�: USAR Moneys PARA prefix Y suffix AUTOM�TICO
%>
<div class="ox-large-format ox-frame"><span class="ox-large-format-prefix"><%=prefix%></span><%=fvalue%><span class="ox-large-format-suffix"><%=suffix%></span></div>