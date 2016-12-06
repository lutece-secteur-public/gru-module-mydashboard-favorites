<jsp:useBean id="managefavorites" scope="session" class="fr.paris.lutece.plugins.mydashboard.modules.favorites.web.FavoriteJspBean" />
<% String strContent =  managefavorites.processController( request, response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>