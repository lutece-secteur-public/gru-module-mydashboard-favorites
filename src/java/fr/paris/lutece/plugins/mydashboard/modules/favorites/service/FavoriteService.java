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

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Category;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.CategoryHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.FavoriteHome;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author alexandre
 */
public class FavoriteService {
    
    private static final String FAVORITES_PROVIDER_NAME = "FAVORITES_PROVIDER";
    private static final String JSON_FAVORITES = "favorites";
    private static final String JSON_ORDER = "order";
    private static final String JSON_LABEL = "label";
    private static final String JSON_URL = "url";
    
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
     * Create an instance of the favorite class
     * @param favorite The instance of the Favorite which contains the informations to store
     * @return The  instance of favorite which has been created with its primary key.
     */
    public Favorite create( Favorite favorite )
    {
        return FavoriteHome.create( favorite );
    }
    
    /**
     * Update favorite
     * @param favorite
     * @return an updated favorite
     */
    public Favorite update( Favorite favorite )
    {
        return FavoriteHome.update( favorite );
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
     * Load all the activated favorites attached to a category
     * @return the list of all the activated favorites datas attached to a category.
     */
    public List<Favorite> findAllActivatedFavoritesByCode( String strCode )
    {
        return FavoriteHome.getActivatedFavoritesListByCode( strCode );
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
    
    /**
     * Return default favorite list
     * @return the default favorite list
     */
    public List<Favorite> findFavoritesByCategoryCode( String categoryCode )
    {
        return FavoriteHome.getFavoritesListByCategoryCode( categoryCode );
    }
    
    /**
     * Return category list
     * @return the category list
     */
    public List<Category> findAllCategories( )
    {
        return CategoryHome.getCategoriesList( );
    }
    
    /**
     * Get user favorites
     * @param strGuid
     * @param nbFavoriteToShow
     * @return json  {favorites:[{"order":1,"label":"test","url":"url"}],[{"order":2,"label":"test2","url":"url"}]}
     */
    @SuppressWarnings( "unchecked" )
    public JSONObject getLastFavoritesJson ( String strGuid, int nNbFavoriteToShow )
    {
        JSONObject jsonResponse = new JSONObject( );
        
        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscriber( strGuid );
        sFilter.setSubscriptionProvider( FAVORITES_PROVIDER_NAME );
                
        List<Subscription> listFavorites = SubscriptionService.getInstance( ).findByFilter( sFilter );
        if( !listFavorites.isEmpty( ) )
        {
            listFavorites = listFavorites.stream( ).sorted( Comparator.comparing( Subscription::getOrder ) ).limit( nNbFavoriteToShow ).collect( Collectors.toList() );
            
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
        return jsonResponse;
    }
    
    /**
     * Return reference list of favorites without category
     * @return the reference list of favorites without category
     */
    public ReferenceList getFavoritesWithoutCategory ( )
    {
        ReferenceList refListFavorites = new ReferenceList();
        List<Favorite> listFavorites = findAllActivatedFavorites( );        
        listFavorites = listFavorites.stream( ).filter( favorite -> StringUtils.isEmpty( favorite.getCategoryCode( ) ) ).collect( Collectors.toList( ) );

        for ( Favorite favorite : listFavorites )
        {
            refListFavorites.addItem( favorite.getId( ), favorite.getLabel( ) );
        }
        
        return refListFavorites;
    }
}
