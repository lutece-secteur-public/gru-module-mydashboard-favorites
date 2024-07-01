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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.web;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Category;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.CategoryHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.FavoriteHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.provider.ProviderFavoriteService;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.fileimage.FileImagePublicService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

/**
 * This class provides the user interface to manage Favorite features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageFavorites.jsp", controllerPath = "jsp/admin/plugins/mydashboard/modules/favorites/", right = "FAVORITES_MANAGEMENT" )
public class FavoriteJspBean extends ManageFavoritesJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_FAVORITES = "/admin/plugins/mydashboard/modules/favorites/manage_favorites.html";
    private static final String TEMPLATE_CREATE_FAVORITE = "/admin/plugins/mydashboard/modules/favorites/create_favorite.html";
    private static final String TEMPLATE_MODIFY_FAVORITE = "/admin/plugins/mydashboard/modules/favorites/modify_favorite.html";
    private static final String TEMPLATE_IMPORT_FAVORITES = "/admin/plugins/mydashboard/modules/favorites/import_favorites.html";

    // Parameters
    private static final String PARAMETER_ID_FAVORITE = "id";
    private static final String PARAMETER_IMPORT_FAVORITES = "import_favorites";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_FAVORITES = "module.mydashboard.favorites.manage_favorites.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_FAVORITE = "module.mydashboard.favorites.modify_favorite.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_FAVORITE = "module.mydashboard.favorites.create_favorite.pageTitle";

    // Markers
    private static final String MARK_FAVORITE_LIST = "favorite_list";
    private static final String MARK_FAVORITE = "favorite";
    private static final String MARK_IMPORT_FAVORITES = "import_favorites";
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_PICTOGRAMME = "pictogramme";
    
    private static final String JSP_MANAGE_FAVORITES = "jsp/admin/plugins/mydashboard/modules/favorites/ManageFavorites.jsp";
    
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_FAVORITE = "module.mydashboard.favorites.message.confirmRemoveFavorite";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "favorites.model.entity.favorite.attribute.";

    // Views
    private static final String VIEW_MANAGE_FAVORITES = "manageFavorites";
    private static final String VIEW_CREATE_FAVORITE = "createFavorite";
    private static final String VIEW_MODIFY_FAVORITE = "modifyFavorite";
    private static final String VIEW_IMPORT_FAVORITES = "importFavorites";

    // Actions
    private static final String ACTION_CREATE_FAVORITE = "createFavorite";
    private static final String ACTION_MODIFY_FAVORITE = "modifyFavorite";
    private static final String ACTION_REMOVE_FAVORITE = "removeFavorite";
    private static final String ACTION_CONFIRM_REMOVE_FAVORITE = "confirmRemoveFavorite";
    private static final String ACTION_TOGGLE_ACTIVATION_FAVORITE = "toggleActivationFavorite";
    private static final String ACTION_IMPORT_FAVORITES = "importFavorites";
    private static final String ACTION_DELETE_PICTOGRAMME = "deletePictogramme";
    
    // Infos
    private static final String INFO_FAVORITE_CREATED = "module.mydashboard.favorites.info.favorite.created";
    private static final String INFO_FAVORITE_UPDATED = "module.mydashboard.favorites.info.favorite.updated";
    private static final String INFO_FAVORITE_REMOVED = "module.mydashboard.favorites.info.favorite.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    //Separators
    private static final String SEPARATOR_IDENTIFIER = "_";
    
    // Session variable to store working values
    private Favorite _favorite;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_FAVORITES, defaultView = true )
    public String getManageFavorites( HttpServletRequest request )
    {
        _favorite = null;
        List<Favorite> listFavorites = FavoriteHome.getFavoritesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_FAVORITE_LIST, listFavorites, JSP_MANAGE_FAVORITES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_FAVORITES, TEMPLATE_MANAGE_FAVORITES, model );
    }

    /**
     * Returns the form to create a favorite
     *
     * @param request The Http request
     * @return the html code of the favorite form
     */
    @View( VIEW_CREATE_FAVORITE )
    public String getCreateFavorite( HttpServletRequest request )
    {
        _favorite = ( _favorite != null ) ? _favorite : new Favorite(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_FAVORITE, _favorite );
        model.put( MARK_CATEGORY_LIST, getCategoryList( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_FAVORITE, TEMPLATE_CREATE_FAVORITE, model );
    }

    /**
     * Process the data capture form of a new favorite
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_FAVORITE )
    public String doCreateFavorite( HttpServletRequest request )
    {
        populate( _favorite, request );
        
        if ( _favorite.getCategoryCode( ).equals( "-1" ) )
        {
            _favorite.setCategoryCode( null );
        }

        // Check constraints
        if ( !validateBean( _favorite, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_FAVORITE );
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        FileItem fileParameterBinaryValue = multipartRequest.getFile( "pictogramme" );
        FileImagePublicService.init( );
        if( fileParameterBinaryValue.getSize( ) > 0 )
        {
            _favorite.setPictogramme( FileImagePublicService.getInstance( ).addImageResource( fileParameterBinaryValue ));
        }
        
        FavoriteHome.create( _favorite );
        addInfo( INFO_FAVORITE_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FAVORITES );
    }

    /**
     * Manages the removal form of a favorite whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_FAVORITE )
    public String getConfirmRemoveFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_FAVORITE ) );
        url.addParameter( PARAMETER_ID_FAVORITE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FAVORITE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a favorite
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage favorites
     */
    @Action( ACTION_REMOVE_FAVORITE )
    public String doRemoveFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );       
        
        Favorite favorite = FavoriteHome.findByPrimaryKey( nId );
        //Delete file
        if ( StringUtils.isNumeric( favorite.getPictogramme( ) ) )
        {
            FileService.getInstance( ).getFileStoreServiceProvider( ).delete( favorite.getPictogramme( ) );
        }
        
        FavoriteService.getInstance( ).removeFavorite( nId );
        
        addInfo( INFO_FAVORITE_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FAVORITES );
    }

    /**
     * Returns the form to update info about a favorite
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_FAVORITE )
    public String getModifyFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );

        if ( _favorite == null || ( _favorite.getId(  ) != nId ))
        {
            _favorite = FavoriteHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_FAVORITE, _favorite );
        model.put( MARK_CATEGORY_LIST, getCategoryList( ) );
        FileImagePublicService.init( );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_FAVORITE, TEMPLATE_MODIFY_FAVORITE, model );
    }

    /**
     * Process the change form of a favorite
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_FAVORITE )
    public String doModifyFavorite( HttpServletRequest request )
    {
        populate( _favorite, request );
        
        if ( _favorite.getCategoryCode( ).equals( "-1" ) )
        {
            _favorite.setCategoryCode( null );
        }

        // Check constraints
        if ( !validateBean( _favorite, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_FAVORITE, PARAMETER_ID_FAVORITE, _favorite.getId( ) );
        }
        
        //Update File
        FileImagePublicService.init( );
        if( StringUtils.isEmpty( _favorite.getPictogramme( ) ) 
                || FileImagePublicService.getInstance( ).getImageResource( Integer.parseInt( _favorite.getPictogramme( ) ) ) == null  )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
            FileItem fileParameterBinaryValue = multipartRequest.getFile( "pictogramme" );
            
            if( fileParameterBinaryValue.getSize( ) > 0 )
            {
                _favorite.setPictogramme( FileImagePublicService.getInstance( ).addImageResource( fileParameterBinaryValue ) );
            }
        }

        FavoriteHome.update( _favorite );
        addInfo( INFO_FAVORITE_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FAVORITES );
    }
    
    /**PROPERTY_PAGE_TITLE_MODIFY_FAVORITE
     * Toggle the activation of a favorite
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_TOGGLE_ACTIVATION_FAVORITE )
    public String doToggleActivationFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );
        Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( nId );
        favorite.setIsActivated( ( favorite.getIsActivated( ) ) ? false : true );

        FavoriteHome.update( favorite );
        addInfo( INFO_FAVORITE_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FAVORITES );
    }
    
    /**
     * Returns the form to import favorites from providers
     *
     * @param request The Http request
     * @return The HTML form to import providers
     */
    @View( VIEW_IMPORT_FAVORITES )
    public String getImportFavorites( HttpServletRequest request )
    {
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_IMPORT_FAVORITES, ProviderFavoriteService.getFavoritesListFromProviderNewRemoteId( ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_FAVORITE, TEMPLATE_IMPORT_FAVORITES, model );
    }
    
    /**
     * Process the import of the favorites
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_IMPORT_FAVORITES )
    public String doImportFavorites( HttpServletRequest request )
    {
        String[] listImportFavoritesChecked = request.getParameterValues( PARAMETER_IMPORT_FAVORITES );
        List<Favorite> listFavoritesToImport = new ArrayList<Favorite>( );
        
        for ( List<Favorite> listFav : ProviderFavoriteService.getFavoritesListFromProvider( ).values( ) )
        {
            for ( Favorite fav : listFav )
            {
                for ( String strFavoriteChecked : listImportFavoritesChecked )
                {
                    if ( ( fav.getProviderName( ) + SEPARATOR_IDENTIFIER + fav.getLabel( ) ).equals( strFavoriteChecked ) )
                    {
                        listFavoritesToImport.add( fav );
                    }
                } 
            }
        }
        
        for ( Favorite fav : listFavoritesToImport )
        {
            FavoriteHome.create( fav );
        }

        return redirectView( request, VIEW_MANAGE_FAVORITES );
    }
    
    private ReferenceList getCategoryList( )
    {
        ReferenceList referenceList = new ReferenceList( );
        
        referenceList.addItem( -1, "-" );
        
        for ( Category category : CategoryHome.getCategoriesList( ) )
        {
            referenceList.addItem( category.getCode( ), category.getName( ) );
        }
        
        return referenceList;
    }
    
    @Action( ACTION_DELETE_PICTOGRAMME )
    public String doDeletePictogramme( HttpServletRequest request )
    {
        String strIdPictogramme = request.getParameter( MARK_PICTOGRAMME );
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );

        if ( _favorite == null || ( _favorite.getId(  ) != nId ) )
        {
            _favorite = FavoriteHome.findByPrimaryKey( nId );
            if( _favorite == null ) throw new AppException( ERROR_RESOURCE_NOT_FOUND );
        }

        if ( StringUtils.isNumeric( strIdPictogramme ) )
        {
            FileService.getInstance( ).getFileStoreServiceProvider( ).delete( strIdPictogramme );
            _favorite.setPictogramme( StringUtils.EMPTY );
            FavoriteHome.update( _favorite );
        }

        return "{\"status\":\"success\"}";
    }
}