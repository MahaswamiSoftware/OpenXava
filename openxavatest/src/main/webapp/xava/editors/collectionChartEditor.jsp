<%-- tmr Move a OpenXava --%>

<%@ page import="org.openxava.model.meta.MetaCollection" %>
<%@ page import="org.openxava.view.View" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
// TMR ME QUED� POR AQU�, YA SACA LOS DATOS. HAY QUE SACAR LOS GR�FICOS.
// tmr �Sacar los datos de @Chart o de MetaChart?
String collectionName = request.getParameter("collectionName");
String viewObject = request.getParameter("viewObject");
View view = (View) context.get(request, viewObject);
MetaCollection collection = view.getMetaModel().getMetaCollection(collectionName);
View subview = view.getSubview(collectionName);

List<Map<String, Object>> values = subview.getCollectionValues();
%>

Esto es un chart para la colecci�n <%=collection.getName()%>:
<ul>
<% for (Map value: values) { %>
<li><%=value%></li>
<% } %>
</ul>

