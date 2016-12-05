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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Favorite objects
 */
public final class FavoriteDAO implements IFavoriteDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_favorite ) FROM mydashboard_mydashboard_favorites_favorite";
    private static final String SQL_QUERY_SELECT = "SELECT id_favorite, label, url, is_activated FROM mydashboard_favorites_favorite WHERE id_favorite = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mydashboard_favorites_favorite ( id_favorite, label, url, is_activated ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mydashboard_favorites_favorite WHERE id_favorite = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mydashboard_favorites_favorite SET id_favorite = ?, label = ?, url = ?, is_activated = ? WHERE id_favorite = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_favorite, label, url, is_activated FROM mydashboard_favorites_favorite";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_favorite FROM mydashboard_favorites_favorite";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );
        int nKey = 1;

        if( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Favorite favorite, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        favorite.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , favorite.getId( ) );
        daoUtil.setString( nIndex++ , favorite.getLabel( ) );
        daoUtil.setString( nIndex++ , favorite.getUrl( ) );
        daoUtil.setBoolean( nIndex++ , favorite.getIsActivated( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Favorite load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        Favorite favorite = null;

        if ( daoUtil.next( ) )
        {
            favorite = new Favorite();
            int nIndex = 1;
            
            favorite.setId( daoUtil.getInt( nIndex++ ) );
            favorite.setLabel( daoUtil.getString( nIndex++ ) );
            favorite.setUrl( daoUtil.getString( nIndex++ ) );
            favorite.setIsActivated( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free( );
        return favorite;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Favorite favorite, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , favorite.getId( ) );
        daoUtil.setString( nIndex++ , favorite.getLabel( ) );
        daoUtil.setString( nIndex++ , favorite.getUrl( ) );
        daoUtil.setBoolean( nIndex++ , favorite.getIsActivated( ) );
        daoUtil.setInt( nIndex , favorite.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Favorite> selectFavoritesList( Plugin plugin )
    {
        List<Favorite> favoriteList = new ArrayList<Favorite>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Favorite favorite = new Favorite(  );
            int nIndex = 1;
            
            favorite.setId( daoUtil.getInt( nIndex++ ) );
            favorite.setLabel( daoUtil.getString( nIndex++ ) );
            favorite.setUrl( daoUtil.getString( nIndex++ ) );
            favorite.setIsActivated( daoUtil.getBoolean( nIndex++ ) );

            favoriteList.add( favorite );
        }

        daoUtil.free( );
        return favoriteList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdFavoritesList( Plugin plugin )
    {
        List<Integer> favoriteList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            favoriteList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return favoriteList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectFavoritesReferenceList( Plugin plugin )
    {
        ReferenceList favoriteList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            favoriteList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return favoriteList;
    }
}