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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.rest.rs;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Rest Constants
 */
public final class Constants 
{
    public static final String API_PATH = "favorites-api";
    public static final String PATH_LAST_FAVORITES = "/lastFavorites";
    public static final String PATH_ALL_FAVORITES = "/allFavorites";
    public static final String PATH_ADD_FAVORITE = "/add";
    public static final String PATH_FAVORITE_ID = "/{id}";
    public static final String PATH_REMOVE_FAVORITE = "/remove" + PATH_FAVORITE_ID;
    
    public static final String PARAMETER_ID = "id";
    
    public static final String RESPONSE_UNAUTHORIZED = "{\"ResponseStatus\": {\"Error\": \"Unauthorized\"}}";
    
    public static final String API_KEY_ERROR = "API KEY is missing or incorrect";
    public static final String CONSTANT_X_API_KEY = "X-API-KEY";
    
    public static final String PROPERTY_REST_API_KEY = AppPropertiesService.getProperty( "favorites.rest.api.key" );
    
    /**
     * Private constructor
     */
    private Constants(  )
    {
    }
}
