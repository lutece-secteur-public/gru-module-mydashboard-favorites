/*
 * Copyright (c) 2002-2024, City of Paris
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

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Category objects
 */
public final class CategoryDAO implements ICategoryDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_category ) FROM mydashboard_favorites_category";
    private static final String SQL_QUERY_SELECT = "SELECT id_category, name, code FROM mydashboard_favorites_category WHERE id_category = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_category, name, code FROM mydashboard_favorites_category";
    private static final String SQL_QUERY_SELECT_BY_CODE = SQL_QUERY_SELECTALL + " WHERE code = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mydashboard_favorites_category ( id_category, name, code ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mydashboard_favorites_category WHERE id_category = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mydashboard_favorites_category SET id_category = ?, name = ?, code = ? WHERE id_category = ?";
    
    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  ) )
        {
            daoUtil.executeQuery( );
            int nKey = 1;

            if( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
            
            return nKey;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Category category, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            category.setId( newPrimaryKey( plugin ) );
            int nIndex = 1;
            
            daoUtil.setInt( nIndex++ , category.getId( ) );
            daoUtil.setString( nIndex++ , category.getName( ) );
            daoUtil.setString( nIndex++ , category.getCode( ) );
            
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Category load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1 , nKey );
            daoUtil.executeQuery( );
            Category category = null;

            if ( daoUtil.next( ) )
            {
                category = new Category();
                int nIndex = 1;
                
                category.setId( daoUtil.getInt( nIndex++ ) );
                category.setName( daoUtil.getString( nIndex++ ) );
                category.setCode( daoUtil.getString( nIndex++ ) );
            }
            
            return category;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1 , nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Category category, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++ , category.getId( ) );
            daoUtil.setString( nIndex++ , category.getName( ) );
            daoUtil.setString( nIndex++ , category.getCode( ) );
            
            daoUtil.setInt( nIndex , category.getId( ) );
            daoUtil.executeUpdate( );
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Category> selectCategoriesList( Plugin plugin )
    {
        List<Category> categoryList = new ArrayList<Category>(  );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                Category category = new Category( );
                int nIndex = 1;
                
                category.setId( daoUtil.getInt( nIndex++ ) );
                category.setName( daoUtil.getString( nIndex++ ) );
                category.setCode( daoUtil.getString( nIndex++ ) );
                
                categoryList.add( category );
            }
        }
        return categoryList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Category findByCode( String strCode, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
            daoUtil.setString( 1 , strCode );
            daoUtil.executeQuery( );
            Category category = null;

            if ( daoUtil.next( ) )
            {
                category = new Category();
                int nIndex = 1;
                
                category.setId( daoUtil.getInt( nIndex++ ) );
                category.setName( daoUtil.getString( nIndex++ ) );
                category.setCode( daoUtil.getString( nIndex++ ) );
            }
            
            return category;
        }
    }
}
