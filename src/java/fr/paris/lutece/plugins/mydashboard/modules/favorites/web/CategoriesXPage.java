/*
 * Copyright (c) 2002-2024, Mairie de Paris
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.CategoriesSubscriptionProviderService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

@Controller( xpageName = "categories" )
public class CategoriesXPage extends MVCApplication
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    //Actions
    private static final String ACTION_ADD_CATEGORY = "add_category";
    private static final String ACTION_DELETE_CATEGORY = "delete_category";
    
    //Parameters
    private static final String PARAMETER_CATEGORY = "category";
    
    //Constants
    private static final String SUBSCRIPTION_KEY = "category_key";
    
    @Action( ACTION_ADD_CATEGORY )
    public XPage doAddCategory( HttpServletRequest request )
    {
        String strIdCategory = request.getParameter( PARAMETER_CATEGORY );
        
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setIdSubscribedResource( strIdCategory );
        sFilter.setSubscriptionProvider( CategoriesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionCategories = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
        
        if ( listSubscriptionCategories.isEmpty() )
        {
            Subscription sub = new Subscription( );
            sub.setIdSubscribedResource( strIdCategory );
            sub.setSubscriptionProvider( CategoriesSubscriptionProviderService.getInstance( ).getProviderName( ) );
            sub.setSubscriptionKey( SUBSCRIPTION_KEY );
            SubscriptionService.getInstance( ).createSubscription( sub, user );
            
            return responseJSON( "{\"success\":\"true\",\"message\":\"category added\"}" );

        }
        
        return responseJSON( "{\"success\":\"false\",\"message\":\"category exist or not found id: " + strIdCategory + "\"}" );

    }
    
    @Action( ACTION_DELETE_CATEGORY )
    public XPage deleteCategory( HttpServletRequest request )
    {
        String strIdCategoryToDelete = request.getParameter( PARAMETER_CATEGORY );
        
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( user.getName( ) );
        sFilter.setIdSubscribedResource( strIdCategoryToDelete );
        sFilter.setSubscriptionProvider( CategoriesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionCategories = SubscriptionService.getInstance( ).findByFilter( sFilter ); 
        
        if ( listSubscriptionCategories.isEmpty() )
        {
            return responseJSON( "{\"success\":\"false\",\"message\":\"no category found for id " + strIdCategoryToDelete + "\"}" );
        }
        
        for (Subscription sub : listSubscriptionCategories)
        {
            SubscriptionService.getInstance( ).removeSubscription( sub, false);
        }
        
        return responseJSON( "{\"success\":\"true\",\"message\":\"category deleted\"}" );
    }
}