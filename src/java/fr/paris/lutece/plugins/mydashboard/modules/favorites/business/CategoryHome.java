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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Category objects
 */
public final class CategoryHome
{
    // Static variable pointed at the DAO instance
    private static ICategoryDAO _dao = SpringContextService.getBean( "categories.categoryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "favorites" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CategoryHome(  )
    {
    }

    /**
     * Create an instance of the category class
     * @param category The instance of the Category which contains the informations to store
     * @return The instance of category which has been created with its primary key.
     */
    public static Category create( Category category )
    {
        _dao.insert( category, _plugin );

        return category;
    }

    /**
     * Update of the category which is specified in parameter
     * @param category The instance of the Category which contains the data to store
     * @return The instance of the category which has been updated
     */
    public static Category update( Category category )
    {
        _dao.store( category, _plugin );

        return category;
    }

    /**
     * Remove the category whose identifier is specified in parameter
     * @param nKey The favorite Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a category whose identifier is specified in parameter
     * @param nKey The category primary key
     * @return an instance of Category
     */
    public static Category findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the favorite objects and returns them as a list
     * @return the list which contains the data of all the favorite objects
     */
    public static List<Category> getCategoriesList( )
    {
        return _dao.selectCategoriesList( _plugin );
    }
    
    /**
     * Get the default favorites list
     * @return the default favorites list
     */
    public static List<Category> getCategoriesListDefault( )
    {
        return _dao.selectDefaultCategoriesList( _plugin );
    }
    
    /**
     * Returns an instance of a category whose identifier is specified in parameter
     * @param nKey The category primary key
     * @return an instance of Category
     */
    public static Category findByCode( String code )
    {
        return _dao.findByCode( code, _plugin);
    }
}

