/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoritesSubscriptionProviderService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;


@Controller( xpageName = "favorites" )
public class FavoritesXPage extends MVCApplication 
{
    
    //View
    private static final String VIEW_FIRST_FAVORITES = "first_favorites";
    
    //Actions
    private static final String ACTION_MODIFY_FAVORITES = "modify_favorites";
    private static final String ACTION_DELETE_FAVORITE = "delete_favorite";
    private static final String ACTION_ADD_FAVORITE = "add_favorite";
    private static final String ACTION_SET_ORDER_FAVORITE = "set_order";
    
    //Encoding
    private static final String ENCODING_DEFAULT = "lutece.encoding.url";
    
    //Parameters
    private static final String PARAMETER_FAVORITES = "favorites";
    private static final String PARAMETER_FAVORITE = "favorite";
    private static final String PARAMETER_NEW_ORDER_FAVORITE = "newOrder";
    private static final String PARAMETER_REDIRECT_URL = "redirect_url";
    
    //Properties
    private static final String PROPERTY_NUMBER_OF_FAVORITES = "favorites.menu.numberOfFavorites.show";
    
    //Constants
    private static final String SUBSCRIPTION_KEY = "favorite_key";
    private static final String FAVORITES_PROVIDER_NAME = "FAVORITES_PROVIDER";
    private static final String JSON_FAVORITES = "favorites";
    private static final String JSON_ORDER = "order";
    private static final String JSON_LABEL = "label";
    private static final String JSON_URL = "url";
    
 
    @Action( ACTION_MODIFY_FAVORITES )
    public XPage modifyFavorites( HttpServletRequest request )
    {
        String[] listFavoritesCheckedId = request.getParameterValues( PARAMETER_FAVORITES );
        String strRedirectUrl = null;
        try
        {
            strRedirectUrl = URLDecoder.decode( request.getParameter( PARAMETER_REDIRECT_URL ), AppPropertiesService.getProperty( ENCODING_DEFAULT ) );
        }
        catch(UnsupportedEncodingException e)
        {
            AppLogService.error( "Unsupported Encoding for decoding "+ PARAMETER_REDIRECT_URL );
        }
        
        //First remove all the favorites subscriptions for the register Lutece user
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
        for (Subscription sub : listSubscriptionFavorites)
        {
            SubscriptionService.getInstance( ).removeSubscription( sub, false);
        }
        
        //Then subscribe new favorites
        if ( listFavoritesCheckedId != null )
        {
            for (String strFavoriteId : listFavoritesCheckedId)
            {
                Subscription sub = new Subscription( );
                sub.setIdSubscribedResource( strFavoriteId );
                sub.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
                sub.setSubscriptionKey( SUBSCRIPTION_KEY );
                SubscriptionService.getInstance( ).createSubscription( sub, user );
            }
        }
        
        return redirect( request, strRedirectUrl );
    }
    
    @Action( ACTION_ADD_FAVORITE )
    public XPage doAddFavorite( HttpServletRequest request )
    {
        String strIdFavorite = request.getParameter( PARAMETER_FAVORITE );
        
        //First remove all the favorites subscriptions for the register Lutece user
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setIdSubscribedResource( strIdFavorite );
        sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
        
        if ( listSubscriptionFavorites.isEmpty() )
        {
            Subscription sub = new Subscription( );
            sub.setIdSubscribedResource( strIdFavorite );
            sub.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
            sub.setSubscriptionKey( SUBSCRIPTION_KEY );
            SubscriptionService.getInstance( ).createSubscription( sub, user );
            
            return responseJSON( "{\"success\":\"true\",\"message\":\"favorite added\"}" );

        }
        
        return responseJSON( "{\"success\":\"false\",\"message\":\"favorite exist or not found id: " + strIdFavorite + "\"}" );

    }
    
    @Action( ACTION_DELETE_FAVORITE )
    public XPage deleteFavorite( HttpServletRequest request )
    {
        String strIdFavoriteToDelete = request.getParameter( PARAMETER_FAVORITE );
        
        //First remove all the favorites subscriptions for the register Lutece user
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setIdSubscribedResource( strIdFavoriteToDelete );
        sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
        
        if ( listSubscriptionFavorites.isEmpty() )
        {
            return responseJSON( "{\"success\":\"false\",\"message\":\"no favorite found for id " + strIdFavoriteToDelete + "\"}" );
        }
        
        for (Subscription sub : listSubscriptionFavorites)
        {
            SubscriptionService.getInstance( ).removeSubscription( sub, false);
        }
        
        return responseJSON( "{\"success\":\"true\",\"message\":\"favorite deleted\"}" );
    }
    
    @Action( ACTION_SET_ORDER_FAVORITE )
    public XPage setOrderFavorite ( HttpServletRequest request )
    {
        String strIdFavorite = request.getParameter( PARAMETER_FAVORITE );
        String strNewOrder = request.getParameter( PARAMETER_NEW_ORDER_FAVORITE );
        
        if ( StringUtils.isNumeric( strIdFavorite ) && StringUtils.isNumeric( strNewOrder ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            SubscriptionFilter sFilter = new SubscriptionFilter( );
            sFilter.setIdSubscriber( user.getName( ) );
            sFilter.setSubscriptionProvider( FavoritesSubscriptionProviderService.getInstance( ).getProviderName( ) );
            List<Subscription> listSubscriptionFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
            
            if ( listSubscriptionFavorites.isEmpty() )
            {
                return responseJSON( "{\"success\":\"false\",\"message\":\"no favorite found for id " + strIdFavorite + "\"}" );
            }
            
            Subscription subToSet = getSubscriptionToSet( strIdFavorite, listSubscriptionFavorites );
            setOrder( listSubscriptionFavorites, strNewOrder, subToSet );
           
            return responseJSON( "{\"success\":\"true\"}" );
        }
        
        return responseJSON( "{\"success\":\"false\"}" );

    }
    
    @SuppressWarnings( "unchecked" )
    @View( VIEW_FIRST_FAVORITES )
    public XPage getFirstFavorites ( HttpServletRequest request  )
    {
        JSONObject jsonResponse = new JSONObject( );
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        
        if( user != null )
        {
            int nNbFavoritesShow = AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_FAVORITES, 4 );
    
            SubscriptionFilter sFilter = new SubscriptionFilter( );
            sFilter.setIdSubscriber( user.getName( ) );
            sFilter.setSubscriptionProvider( FAVORITES_PROVIDER_NAME );
                    
            List<Subscription> listFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter );
            if( !listFavorites.isEmpty( ) )
            {
                listFavorites = listFavorites.stream( ).sorted( Comparator.comparing( Subscription::getOrder ) ).limit( nNbFavoritesShow ).collect( Collectors.toList() );
                
                List<JSONObject> jsonFavorites = new ArrayList<>();
                for ( Subscription sub : listFavorites )
                {
                    JSONObject jsonFavorite = new JSONObject( );
    
                    Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( Integer.parseInt( sub.getIdSubscribedResource( ) ) );
                    jsonFavorite.put( JSON_ORDER, sub.getOrder( ) );
                    jsonFavorite.put( JSON_LABEL, favorite.getLabel( ) );
                    jsonFavorite.put( JSON_URL, favorite.getUrl( ) );
                    
                    jsonFavorites.add( jsonFavorite );
                }
                jsonResponse.put( JSON_FAVORITES, jsonFavorites );               
            }
        }
        return responseJSON( jsonResponse.toJSONString( ) );
    }
    
    /**
     * Set order
     * @param listSubscriptionFavorites
     * @param strNewOrder
     * @param subToSet
     */
    private void setOrder( List<Subscription> listSubscriptionFavorites, String strNewOrder, Subscription subToSet )
    {
        int nNewOrder = Integer.parseInt( strNewOrder );
        int nOrderInit = 1;
        for ( Subscription sub : listSubscriptionFavorites )
        {
            if( subToSet != null && !subToSet.getIdSubscribedResource( ).equals( sub.getIdSubscribedResource( ) ) )
            {
                if ( nNewOrder < subToSet.getOrder( ) && sub.getOrder( ) >= nNewOrder 
                        && sub.getOrder( ) < subToSet.getOrder( ) )
                {
                    sub.setOrder( sub.getOrder( ) + 1 );
                    SubscriptionService.getInstance( ).setSubscription( sub );
                }
                else if ( nNewOrder > subToSet.getOrder( ) && sub.getOrder( ) <= nNewOrder 
                        && sub.getOrder( ) > subToSet.getOrder( ))
                {
                    sub.setOrder( sub.getOrder( ) -1 );
                    SubscriptionService.getInstance( ).setSubscription( sub );
                }
                else if ( subToSet.getOrder( ) == sub.getOrder( ) && nOrderInit != nNewOrder)
                {
                    sub.setOrder( nOrderInit );
                    SubscriptionService.getInstance( ).setSubscription( sub );
                }
            }
            nOrderInit++;
        }
        
        if( subToSet != null )
        {
            subToSet.setOrder( nNewOrder );
            SubscriptionService.getInstance( ).setSubscription( subToSet ); 
        }
    }

    /**
     * Get subscription to set
     * @param strIdFavorite
     * @param listSubscriptionFavorites
     * @return
     */
    private Subscription getSubscriptionToSet( String strIdFavorite, List<Subscription> listSubscriptionFavorites )
    {
        for ( Subscription sub : listSubscriptionFavorites)
        {
            if( sub.getIdSubscribedResource( ).equals( strIdFavorite ) )
            {
                return sub;
            }
        }
        return null;
    }
}
