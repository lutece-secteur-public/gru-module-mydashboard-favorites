<jsp:useBean id="manageCategories" scope="session" class="fr.paris.lutece.plugins.mydashboard.modules.favorites.web.CategoryJspBean" />
<% String strContent =  manageCategories.processController( request, response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>