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

import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoritesSubscriptionProviderService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


@Controller( xpageName = "favorites" )
public class MyDashBoardFavoritesXPage extends MVCApplication 
{
    
    //Actions
    private static final String ACTION_MODIFY_FAVORITES = "modify_favorites";
    
    //Encoding
    private static final String ENCODING_DEFAULT = "lutece.encoding.url";
    
    //Parameters
    private static final String PARAMETER_FAVORITES = "favorites";
    private static final String PARAMETER_REDIRECT_URL = "redirect_url";
    
    //Subscription key for favorites
    private static final String SUBSCRIPTION_KEY = "favorite_key";
    
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
}
