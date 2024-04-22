/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
 package fr.paris.lutece.plugins.mydashboard.modules.favorites.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Favorite objects
 */
public final class FavoriteHome
{
    // Static variable pointed at the DAO instance
    private static IFavoriteDAO _dao = SpringContextService.getBean( "favorites.favoriteDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "favorites" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private FavoriteHome(  )
    {
    }

    /**
     * Create an instance of the favorite class
     * @param favorite The instance of the Favorite which contains the informations to store
     * @return The  instance of favorite which has been created with its primary key.
     */
    public static Favorite create( Favorite favorite )
    {
        _dao.insert( favorite, _plugin );

        return favorite;
    }

    /**
     * Update of the favorite which is specified in parameter
     * @param favorite The instance of the Favorite which contains the data to store
     * @return The instance of the  favorite which has been updated
     */
    public static Favorite update( Favorite favorite )
    {
        _dao.store( favorite, _plugin );

        return favorite;
    }

    /**
     * Remove the favorite whose identifier is specified in parameter
     * @param nKey The favorite Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a favorite whose identifier is specified in parameter
     * @param nKey The favorite primary key
     * @return an instance of Favorite
     */
    public static Favorite findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the favorite objects and returns them as a list
     * @return the list which contains the data of all the favorite objects
     */
    public static List<Favorite> getFavoritesList( )
    {
        return _dao.selectFavoritesList( _plugin );
    }
    
    /**
     * Load the data of all the activated favorite objects and returns them as a list
     * @return the list which contains the data of all the favorite objects
     */
    public static List<Favorite> getActivatedFavoritesList( )
    {
        return _dao.selectActivatedFavoritesList( _plugin );
    }
    
    /**
     * Load the id of all the favorite objects and returns them as a list
     * @return the list which contains the id of all the favorite objects
     */
    public static List<Integer> getIdFavoritesList( )
    {
        return _dao.selectIdFavoritesList( _plugin );
    }
    
    /**
     * Load the data of all the favorite objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the favorite objects
     */
    public static ReferenceList getFavoritesReferenceList( )
    {
        return _dao.selectFavoritesReferenceList(_plugin );
    }
    
    /**
     * Return the number of favorite for one id remote and one provider name
     * @param strProviderName the favorite provider name
     * @param strRemoteId the favorite remote id
     * @param plugin the Plugin
     * @return the number of favorite for one id remote and one provider name
     */
    public static int getNumberFavoriteProviderRemoteId( String strProviderName, String strRemoteId )
    {
        return _dao.countProviderNameRemoteId( strProviderName, strRemoteId, _plugin );
    }
    
    /**
     * Get the default favorites list
     * @return the default favorites list
     */
    public static List<Favorite> getFavoritesListDefault( )
    {
        return _dao.selectDefaultFavoritesList( _plugin );
    }
    
    /**
     * Return the number of favorite for one id remote and one provider name
     * @param strCategoryCode the category code
     * @return the List which contains the data of all the favorites filtered by the category code
     */
    public static List<Favorite> getFavoritesListByCategoryCode( String strCategoryCode )
    {
        return _dao.selectCategoryCodeFavoritesList( strCategoryCode, _plugin );
    }
}

