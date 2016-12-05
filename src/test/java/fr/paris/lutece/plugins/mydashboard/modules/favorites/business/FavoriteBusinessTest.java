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

import fr.paris.lutece.test.LuteceTestCase;


public class FavoriteBusinessTest extends LuteceTestCase
{
    private final static String LABEL1 = "Label1";
    private final static String LABEL2 = "Label2";
    private final static String URL1 = "Url1";
    private final static String URL2 = "Url2";
	private final static boolean ISACTIVATED1 = true;
    private final static boolean ISACTIVATED2 = false;

    public void testBusiness(  )
    {
        // Initialize an object
        Favorite favorite = new Favorite();
        favorite.setLabel( LABEL1 );
        favorite.setUrl( URL1 );
        favorite.setIsActivated( ISACTIVATED1 );

        // Create test
        FavoriteHome.create( favorite );
        Favorite favoriteStored = FavoriteHome.findByPrimaryKey( favorite.getId( ) );
        assertEquals( favoriteStored.getLabel() , favorite.getLabel( ) );
        assertEquals( favoriteStored.getUrl() , favorite.getUrl( ) );
        assertEquals( favoriteStored.getIsActivated() , favorite.getIsActivated( ) );

        // Update test
        favorite.setLabel( LABEL2 );
        favorite.setUrl( URL2 );
        favorite.setIsActivated( ISACTIVATED2 );
        FavoriteHome.update( favorite );
        favoriteStored = FavoriteHome.findByPrimaryKey( favorite.getId( ) );
        assertEquals( favoriteStored.getLabel() , favorite.getLabel( ) );
        assertEquals( favoriteStored.getUrl() , favorite.getUrl( ) );
        assertEquals( favoriteStored.getIsActivated() , favorite.getIsActivated( ) );

        // List test
        FavoriteHome.getFavoritesList();

        // Delete test
        FavoriteHome.remove( favorite.getId( ) );
        favoriteStored = FavoriteHome.findByPrimaryKey( favorite.getId( ) );
        assertNull( favoriteStored );
        
    }

}