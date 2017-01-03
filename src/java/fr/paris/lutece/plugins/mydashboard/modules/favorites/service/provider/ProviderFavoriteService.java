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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.service.provider;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.FavoriteHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderFavoriteService
{
    private  static ProviderFavoriteService _instance = null ;
    
    private ProviderFavoriteService(  )
    {
        
    };
    
    /**
     * Get the instance of this service singleton
     * @return the singleton of this service
     */
    public static ProviderFavoriteService getInstance( )
    {
        if ( _instance == null )
        {
            _instance = new ProviderFavoriteService( );
            return _instance ; 
        }
        else 
        {
            return _instance;
        }
    }
    
    /**
     * Get a map with providerName ==> list of Favorites objects for the providerName
     * @return the Map with the list of Favorites corresponding to a providerName
     */
    public static Map<String, List<Favorite> > getFavoritesListFromProvider( )
    {
        Map<String, List<Favorite> > mapFavoritesProvider = new HashMap<String, List<Favorite>>( );
        for (IFavoriteProvider provider :  SpringContextService.getBeansOfType( IFavoriteProvider.class ) )
        {
            mapFavoritesProvider.put( provider.getFavoriteProviderName( ), provider.getProvideFavorites( ) );
        }
        return mapFavoritesProvider;
    }
    
    /**
     * Get a map with providerName ==> list of Favorites objects for the providerName only for remote id not ever registered for this provider
     * @return the Map with the list of Favorites corresponding to a providerName only for remote id not ever registered for this provider
     */
    public static Map<String, List<Favorite> > getFavoritesListFromProviderNewRemoteId( )
    {
        Map<String, List<Favorite> > mapFavoritesProvider = getFavoritesListFromProvider( );
        Map<String, List<Favorite> > mapFavoritesToReturn = new HashMap<String,List<Favorite> > ( );
        
        for ( Map.Entry<String, List<Favorite> > entry : mapFavoritesProvider.entrySet( ) )
        {
            List<Favorite> listFavoriteAfterFilter = new ArrayList<Favorite>( );
            for ( Favorite fav : entry.getValue( ) )
            {
                if ( FavoriteHome.getNumberFavoriteProviderRemoteId( fav.getProviderName( ), fav.getRemoteId( ) ) == 0 )
                {
                    listFavoriteAfterFilter.add( fav );
                }
            }
            if ( listFavoriteAfterFilter.size( ) > 0 )
            {
                mapFavoritesToReturn.put( entry.getKey( ), listFavoriteAfterFilter );
            }
        }
        return mapFavoritesToReturn;
    }
}
