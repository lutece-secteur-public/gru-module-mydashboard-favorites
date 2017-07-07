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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.service;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.FavoriteHome;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import java.util.List;

/**
 *
 * @author alexandre
 */
public class FavoriteService {
    
    private  static FavoriteService _instance = null ;
    
    private FavoriteService(  )
    {
        
    };
    
    /**
     * Get the instance of this service singleton
     * @return the singleton of this service
     */
    public static FavoriteService getInstance( )
    {
        if ( _instance == null )
        {
            _instance = new FavoriteService( );
            return _instance ; 
        }
        else 
        {
            return _instance;
        }
    }
    
    /**
     * Load the favorite for the given id
     * @param idFavorite the id of the favorite to get
     * @return the favorite data corresponding to the provided id.
     */
    public Favorite findByPrimaryKey( int idFavorite )
    {
        return FavoriteHome.findByPrimaryKey( idFavorite );
    }
    
    /**
     * Load all the favorites
     * @return the list of all the favorites datas.
     */
    public List<Favorite> findAllFavorites( )
    {
        return FavoriteHome.getFavoritesList( );
    }
    
    /**
     * Load all the activated favorites
     * @return the list of all the activated favorites datas.
     */
    public List<Favorite> findAllActivatedFavorites( )
    {
        return FavoriteHome.getActivatedFavoritesList( );
    }
    
    /**
     * Remove favorite with specific id, and delete all the users subscription to that favorite
     * @param nIdFavorite the id of the favorite to delete
     */
    public void removeFavorite( int nIdFavorite )
    {
        //Remove all the subscription to the favorite the user want to delete
        SubscriptionFilter filter = new SubscriptionFilter( );
        filter.setIdSubscribedResource( Integer.toString( nIdFavorite ) );
        List<Subscription> listSubscription = SubscriptionService.getInstance( ).findByFilter( filter );
        for ( Subscription subscription : listSubscription )
        {
            SubscriptionService.getInstance( ).removeSubscription( subscription, false );
        }
        //Remove the favorite 
        FavoriteHome.remove( nIdFavorite );
    }
    
    /**
     * Return default favorite list
     * @return the default favorite list
     */
    public List<Favorite> findAllDefaultFavorites( )
    {
        return FavoriteHome.getFavoritesListDefault( );
    }
}
