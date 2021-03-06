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
import java.util.ArrayList;
import java.util.List;


public class MokeFavoriteProvider implements IFavoriteProvider
{
    
    private static final String FAVORITES_MOKE_PROVIDER_NAME = "FAVORITES_MOKE_PROVIDER";
    
    @Override 
    public List<Favorite> getProvideFavorites( )
    {
        List<Favorite> listFavorites = new ArrayList<Favorite>();
        
        Favorite fav1 = new Favorite( );
        fav1.setId( 0 );
        fav1.setIsActivated( true );
        fav1.setLabel( "fav1" );
        fav1.setProviderName( FAVORITES_MOKE_PROVIDER_NAME );
        fav1.setRemoteId( "0" );
        fav1.setIsDefault( false );
        fav1.setUrl( "http://google.fr" );
        
        Favorite fav2 = new Favorite( );
        fav2.setId( 1 );
        fav2.setIsActivated( true );
        fav2.setLabel( "fav2" );
        fav2.setProviderName( FAVORITES_MOKE_PROVIDER_NAME );
        fav2.setRemoteId( "1" );
        fav1.setIsDefault( false );
        fav2.setUrl( "http://yahoo.fr" );
        
        listFavorites.add( fav1 );
        listFavorites.add( fav2 );
        
        return listFavorites;
    }
    
    @Override
    public String getFavoriteProviderName( )
    {
        return FAVORITES_MOKE_PROVIDER_NAME;
    }
    
}
