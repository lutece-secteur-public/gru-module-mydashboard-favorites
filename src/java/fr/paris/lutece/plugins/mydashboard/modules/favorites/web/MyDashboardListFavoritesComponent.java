/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mydashboard.modules.favorites.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoritesSubscriptionProviderService;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardListFavoritesComponent
 *
 */
public class MyDashboardListFavoritesComponent extends MyDashboardComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final String DASHBOARD_COMPONENT_ID = "mydashboard.myDashboardComponentListFavorites";
    private static final String MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION = "module.mydashboard.favorites.component.description.list_favorites";
    private static final String TEMPLATE_DASHBOARD_COMPONENT = "skin/plugins/mydashboard/modules/favorites/list_favorites_component.html";

    private static final String MARK_FAVORITES = "favorites";
    
    private static final String PARAMETER_ALL = "all";
    private static final String PARAMETER_CATEGORY_CODE = "cat";
    
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap< >(  );
        
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        
        //research by filter on user guid and for favorites provider
        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter );  
        //get favorites about those subscriptions
        List<Favorite> listFavoritesSuscribed = new ArrayList<>( );
        
        for ( Subscription sub : listFavorites )
        {
            Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( Integer.parseInt( sub.getIdSubscribedResource( ) ) );
            if ( PARAMETER_ALL.equals( request.getParameter( PARAMETER_CATEGORY_CODE ) ) || ( favorite.getCategoryCode( ) != null && favorite.getCategoryCode( ).equals( request.getParameter( PARAMETER_CATEGORY_CODE ) ) ) )
            {
                favorite.setOrder( sub.getOrder( ) );
                listFavoritesSuscribed.add( favorite );
            }
        }

        model.put( MARK_FAVORITES, listFavoritesSuscribed );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT, LocaleService.getDefault( ), model );

        return template.getHtml( );
        
    }

    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION, locale );
    }

    
    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

}
