<%@ page errorPage="../../../../ErrorPage.jsp" %>

<jsp:include page="../../../../AdminHeader.jsp" />

<jsp:useBean id="managefavorites" scope="session" class="fr.paris.lutece.plugins.mydashboard.modules.favorites.web.ManageFavoritesJspBean" />

<% managefavorites.init( request, managefavorites.RIGHT_MANAGEFAVORITES ); %>
<%= managefavorites.getManageFavoritesHome ( request ) %>

<%@ include file="../../../../AdminFooter.jsp" %>
