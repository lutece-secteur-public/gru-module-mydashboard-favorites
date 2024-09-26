/*
 * Copyright (c) 2002-2024, Mairie de Paris
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.FavoriteHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

/**
 * 
 * FavoritesRest
 *
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH )
public class FavoritesRest
{
    //Properties
    private static final String PROPERTY_NUMBER_OF_FAVORITES = "favorites.menu.numberOfFavorites.show";
    
    private static ObjectMapper _mapper;
    
    public FavoritesRest( )
    {
        _mapper = new ObjectMapper( ).configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false )
        .configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false ).configure( SerializationFeature.WRAP_ROOT_VALUE, false );
    }
    
    /**
     * Get last favorites
     * @param request
     * @param nNbFavoritesToShow
     * @return Json
     */
    @GET
    @Path( Constants.PATH_LAST_FAVORITES )
    @Produces ( MediaType.APPLICATION_JSON  )
    public Response getFavorites ( @Context HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        
        if( user != null )
        {         
            JSONObject jsonResponse = FavoriteService.getInstance( ).getLastFavoritesJson( user.getName( ), AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_FAVORITES, 4 ));
            
            return Response.ok( jsonResponse ).build( );
        }
        
        return Response.status( Response.Status.UNAUTHORIZED ).entity( Constants.RESPONSE_UNAUTHORIZED).build( );
    }
    
    /**
     * Get all favorites
     * @param request
     * @param nNbFavoritesToShow
     * @return Json
     */
    @GET
    @Path( Constants.PATH_ALL_FAVORITES )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces ( MediaType.APPLICATION_JSON  )
    public Response getAllFavorites ( @Context HttpServletRequest request )
    {   
        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( FavoriteHome.getFavoritesList( ) ) ) ).build( );
    }
    
    @POST
    @Path( Constants.PATH_ADD_FAVORITE )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addFavorite( String strFavoriteJson)
    {
        try
        {
            Favorite favorite = _mapper.readValue( strFavoriteJson, Favorite.class );
            
            if ( favorite != null )
            {
                FavoriteService.getInstance( ).create( favorite );
                
                return Response.status( Response.Status.CREATED ).entity( JsonUtil.buildJsonResponse( new JsonResponse( favorite ) ) ).build( );
            }
            
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.getReasonPhrase( ) ) ) ).build( );
        } 
        catch ( JsonProcessingException e )
        {
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.getReasonPhrase( ) ) ) ).build( );
        }
    }
    
    @DELETE
    @Path( Constants.PATH_FAVORITE_ID )
    @Produces( MediaType.APPLICATION_JSON )
    public Response removeFavorite( @PathParam( Constants.PARAMETER_ID ) Integer id )
    {
        Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( id );
        
        if ( favorite != null )
        {
            //Remove all the subscription to the favorite
            SubscriptionFilter filter = new SubscriptionFilter( );
            filter.setIdSubscribedResource( Integer.toString( id ) );
            List<Subscription> listSubscription = SubscriptionService.getInstance( ).findByFilter( filter );
            for ( Subscription subscription : listSubscription )
            {
                SubscriptionService.getInstance( ).removeSubscription( subscription, false );
            }
            
            FavoriteService.getInstance( ).removeFavorite( id );
            
            return Response.status( Response.Status.OK ).build( );
        }
        
        return Response.status( Response.Status.NOT_FOUND ).build( );
    }
}
