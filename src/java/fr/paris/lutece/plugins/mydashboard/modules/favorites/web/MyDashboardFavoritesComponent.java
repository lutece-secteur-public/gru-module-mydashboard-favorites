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

import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoritesSubscriptionProviderService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.util.UrlUtil;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import java.util.ArrayList;

public class MyDashboardFavoritesComponent extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "mydashboard.myDashboardComponentFavorite";
    private static final String MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION = "module.mydashboard.favorites.component.description";
    private static final String TEMPLATE_DASHBOARD_COMPONENT = "skin/plugins/mydashboard/modules/favorites/favorites_component.html";
    
    //Subscription providers 

    //Markers
    private static final String MARK_FAVORITES_CHECKED_LIST = "favorites_checked_list";
    private static final String MARK_FAVORITES_LIST = "favorites_list";
    private static final String MARK_REDIRECT_URL = "redirect_url";
    
    private static final String PARAMETER_ALL = "all";
    private static final String PARAMETER_CATEGORY_CODE = "cat";
    
    private static final String PROPERTY_NUMBER_OF_FAVORITES_MAX = "favorites.dashboard.number.favorites.max";

    @Override
    public String getDashboardData( HttpServletRequest request )  
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        //research by filter on user guid and for favorites provider
        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listFavoritesSubscription = SubscriptionService.getInstance( ).findByFilter( sFilter );  
        //get favorites about those subscriptions
        List<Favorite> listFavoritesSuscribed = new ArrayList<Favorite>( );
        String strCategoryCode = request.getParameter( PARAMETER_CATEGORY_CODE );
        List<Favorite> favoritesList = new ArrayList<Favorite>( );
        if ( strCategoryCode != null )
        {
            if ( PARAMETER_ALL.equals( strCategoryCode ) )
            {
                favoritesList = FavoriteService.getInstance( ).findAllActivatedFavorites( );
            }
            else
            {
                favoritesList = FavoriteService.getInstance( ).findAllActivatedFavoritesByCode( strCategoryCode );
            }
        }
        for ( Subscription sub : listFavoritesSubscription )
        {
            for ( Favorite favorite : favoritesList )
            {
                if ( String.valueOf( favorite.getId( ) ).equals( sub.getIdSubscribedResource( ) ) && ( PARAMETER_ALL.equals( strCategoryCode ) || ( favorite.getCategoryCode( ) != null && favorite.getCategoryCode( ).equals( strCategoryCode ) ) ) )
                {
                    favorite.setOrder( sub.getOrder( ) );
                    listFavoritesSuscribed.add( favorite );
                }
            }
        }
        int maxFavorite = AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_FAVORITES_MAX, 6 );
        if ( listFavoritesSuscribed.size( ) > maxFavorite )
        {
            listFavoritesSuscribed = listFavoritesSuscribed.subList( 0, maxFavorite );
        }

        model.put( MARK_FAVORITES_CHECKED_LIST, listFavoritesSuscribed );
        model.put( MARK_FAVORITES_LIST , favoritesList );
        model.put( MARK_REDIRECT_URL, EncodingService.encodeUrl( UrlUtil.getFullUrl( request ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT, LocaleService.getDefault( ), model );

        return template.getHtml( );
    }
    


    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION, locale );
    }

}
