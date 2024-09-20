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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.web;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Category;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.CategoryHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Favorite;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.CategoriesSubscriptionProviderService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.provider.DemandTypeService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides the user interface to manage Category features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCategories.jsp", controllerPath = "jsp/admin/plugins/mydashboard/modules/favorites/", right = "CATEGORIES_MANAGEMENT" )
public class CategoryJspBean extends ManageCategoriesJspBean
{
    private static final long serialVersionUID = 1L;
    // Templates
    private static final String TEMPLATE_MANAGE_CATEGORIES = "/admin/plugins/mydashboard/modules/favorites/manage_categories.html";
    private static final String TEMPLATE_CREATE_CATEGORY = "/admin/plugins/mydashboard/modules/favorites/create_category.html";
    private static final String TEMPLATE_MODIFY_CATEGORY = "/admin/plugins/mydashboard/modules/favorites/modify_category.html";
    
    // Parameters
    private static final String PARAMETER_ID_CATEGORY = "id";
    private static final String PARAMETER_ID_FAVORITE = "idFavorite";
    private static final String PARAMETER_ID_DEMAND_TYPE = "idDemandType";
    
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CATEGORIES = "module.mydashboard.favorites.manage_categories.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CATEGORY = "module.mydashboard.favorites.modify_category.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CATEGORY = "module.mydashboard.favorites.create_category.pageTitle";

    // Markers
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_CATEGORY = "category";
    private static final String MARK_FAVORITES_WITHOUT_CAT = "favoritesWithoutCat";
    private static final String MARK_FAVORITES_ASSOCIATED = "favoritesAssociated";
    private static final String MARK_DEMAND_TYPES_WITHOUT_CAT = "demandTypesWithoutCat";
    private static final String MARK_DEMAND_TYPES_ASSOCIATED = "demandTypesAssociated";
           
    private static final String JSP_MANAGE_CATEGORIES = "jsp/admin/plugins/mydashboard/modules/favorites/ManageCategories.jsp";
    
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CATEGORY = "module.mydashboard.favorites.message.confirmRemoveCategory";
    private static final String MESSAGE_CONFIRM_REMOVE_ASSOCIATED_FAVORITE = "module.mydashboard.favorites.message.confirmRemoveAssociatedFavorite";
    private static final String MESSAGE_CONFIRM_REMOVE_ASSOCIATED_DEMAND_TYPE = "module.mydashboard.favorites.message.confirmRemoveAssociatedDemandType";
    
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "favorites.model.entity.category.attribute.";

    // Views
    private static final String VIEW_MANAGE_CATEGORIES = "manageCategories";
    private static final String VIEW_CREATE_CATEGORY = "createCategory";
    private static final String VIEW_MODIFY_CATEGORY = "modifyCategory";
    
    // Actions
    private static final String ACTION_CREATE_CATEGORY = "createCategory";
    private static final String ACTION_MODIFY_CATEGORY = "modifyCategory";
    private static final String ACTION_REMOVE_CATEGORY = "removeCategory";
    private static final String ACTION_CONFIRM_REMOVE_CATEGORY = "confirmRemoveCategory";
    private static final String ACTION_REMOVE_ASSOCIATED_FAVORITE = "removeAssociatedFavorite";
    private static final String ACTION_CONFIRM_REMOVE_ASSOCIATED_FAVORITE  = "confirmRemoveAssociatedFavorite";
    private static final String ACTION_ASSOCIATE_FAVORITE_CATEGORY = "associateFavoriteCategory";   
    private static final String ACTION_REMOVE_ASSOCIATED_DEMAND_TYPE = "removeAssociatedDemandType";
    private static final String ACTION_CONFIRM_REMOVE_ASSOCIATED_DEMAND_TYPE  = "confirmRemoveAssociatedDemandType";
    private static final String ACTION_ASSOCIATE_DEMAND_TYPE_CATEGORY = "associateDemandTypeCategory";
    
    // Infos
    private static final String INFO_CATEGORY_CREATED = "module.mydashboard.favorites.info.category.created";
    private static final String INFO_CATEGORY_UPDATED = "module.mydashboard.favorites.info.category.updated";
    private static final String INFO_CATEGORY_REMOVED = "module.mydashboard.favorites.info.category.removed";
    private static final String INFO_ASSOCIATION_FAVORITE_CREATED = "module.mydashboard.favorites.info.association.favorite.created";    
    private static final String INFO_ASSOCIATION_FAVORITE_REMOVED = "module.mydashboard.favorites.info.association.favorite.removed";
    private static final String INFO_ASSOCIATION_DEMAND_TYPE_CREATED = "module.mydashboard.favorites.info.association.demand_type.created";    
    private static final String INFO_ASSOCIATION_DEMAND_TYPE_REMOVED = "module.mydashboard.favorites.info.association.demand_type.removed";   
    private static final String ERROR_CATEGORY_CODE_ALREADY_EXIST = "module.mydashboard.favorites.error.category.code.already.exist";
    private static final String ERROR_CATEGORY_DELETE_HAS_FAVORITE = "module.mydashboard.favorites.error.category.delete.still.has.favorite";
    private static final String ERROR_CATEGORY_DELETE_HAS_DEMAND_TYPE = "module.mydashboard.favorites.error.category.delete.still.has.demand_type";
    
    // Session variable to store working values
    private Category _category;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CATEGORIES, defaultView = true )
    public String getManageCategories( HttpServletRequest request )
    {
        _category = null;
        List<Category> listCategories = CategoryHome.getCategoriesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CATEGORY_LIST, listCategories, JSP_MANAGE_CATEGORIES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CATEGORIES, TEMPLATE_MANAGE_CATEGORIES, model );
    }

    /**
     * Returns the form to create a category
     *
     * @param request The Http request
     * @return the html code of the category form
     */
    @View( VIEW_CREATE_CATEGORY )
    public String getCreateCategory( HttpServletRequest request )
    {
        _category = ( _category != null ) ? _category : new Category( );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATEGORY, _category );
        model.put( MARK_FAVORITES_WITHOUT_CAT, FavoriteService.getInstance( ).getFavoritesWithoutCategory( ) );
        model.put( MARK_DEMAND_TYPES_WITHOUT_CAT, DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).getRefListDemandTypeWithoutCategory( ) );
        
        return getPage( PROPERTY_PAGE_TITLE_CREATE_CATEGORY, TEMPLATE_CREATE_CATEGORY, model );
    }

    /**
     * Process the data capture form of a new category
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CATEGORY )
    public String doCreateCategory( HttpServletRequest request )
    {
        populate( _category, request );

        // Check constraints
        if ( !validateBean( _category, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CATEGORY );
        }
        
        if ( CategoryHome.findByCode( _category.getCode( ) ) != null )
        {
            addError( ERROR_CATEGORY_CODE_ALREADY_EXIST, getLocale( ) );
            return redirectView( request, VIEW_CREATE_CATEGORY );
        }
        
        CategoryHome.create( _category );
        
        addCategeryToFavorites( request );        
        addCategoryToDemandTypes( request );
        
        addInfo( INFO_CATEGORY_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CATEGORIES );
    }

    /**
     * Add category to favorites
     * @param request
     */
    private void addCategeryToFavorites( HttpServletRequest request )
    {
        //Bind favorites with category
        String[] listIdFavorites = request.getParameterValues( PARAMETER_ID_FAVORITE );
        if( listIdFavorites != null && listIdFavorites.length > 0 )
        {
            for( String idFavorites : listIdFavorites )
            {
                Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( Integer.parseInt( idFavorites ) );
                if( favorite != null && StringUtils.isEmpty( favorite.getCategoryCode( ) ) )
                {
                    favorite.setCategoryCode( _category.getCode( ) );
                    FavoriteService.getInstance( ).update( favorite );
                }              
            }
        }
    }

    /**
     * Add category to demand types
     * @param request
     */
    private void addCategoryToDemandTypes( HttpServletRequest request )
    {
        //Bind demand types with category
        String[] listIdDemandTypes = request.getParameterValues( PARAMETER_ID_DEMAND_TYPE );
        if( listIdDemandTypes != null && listIdDemandTypes.length > 0 )
        {
            for( String idDemandType : listIdDemandTypes )
            {
                DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).updateDemandType( Integer.parseInt( idDemandType ), _category.getCode( ) );
            }
        }
    }

    /**
     * Manages the removal form of a category whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CATEGORY )
    public String getConfirmRemoveCategory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATEGORY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CATEGORY ) );
        url.addParameter( PARAMETER_ID_CATEGORY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CATEGORY, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a category
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage categories
     */
    @Action( ACTION_REMOVE_CATEGORY )
    public String doRemoveCategory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATEGORY ) );
        
        Category catory = CategoryHome.findByPrimaryKey( nId );
        
        List<Favorite> listFavorite = FavoriteService.getInstance( ).findFavoritesByCategoryCode( catory.getCode( ) );
        
        ReferenceList refDemandTypes = DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).getRefListDemandType( catory.getCode( ) );
        
        if ( listFavorite != null && !listFavorite.isEmpty( ) )
        {
            addError( ERROR_CATEGORY_DELETE_HAS_FAVORITE, getLocale( ) );
        }
        else if ( !refDemandTypes.isEmpty( ) )
        {
            addError( ERROR_CATEGORY_DELETE_HAS_DEMAND_TYPE, getLocale( ) );
        }
        else
        {
            removeAllCategorySubscriptions( String.valueOf( nId ) );
            CategoryHome.remove( nId );
            addInfo( INFO_CATEGORY_REMOVED, getLocale( ) );
        }

        return redirectView( request, VIEW_MANAGE_CATEGORIES );
    }

    /**
     * Returns the form to update info about a category
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CATEGORY )
    public String getModifyCategory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATEGORY ) );

        if ( _category == null || ( _category.getId(  ) != nId ))
        {
            _category = CategoryHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATEGORY, _category );
        model.put( MARK_FAVORITES_WITHOUT_CAT, FavoriteService.getInstance( ).getFavoritesWithoutCategory( ) );
        model.put( MARK_FAVORITES_ASSOCIATED, FavoriteService.getInstance( ).findAllActivatedFavoritesByCode( _category.getCode( ) ) );
        model.put( MARK_DEMAND_TYPES_WITHOUT_CAT, DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).getRefListDemandTypeWithoutCategory( ) );
        model.put( MARK_DEMAND_TYPES_ASSOCIATED, DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).getRefListDemandType( _category.getCode( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CATEGORY, TEMPLATE_MODIFY_CATEGORY, model );
    }

    /**
     * Process the change form of a category
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CATEGORY )
    public String doModifyCategory( HttpServletRequest request )
    {
        populate( _category, request );

        // Check constraints
        if ( !validateBean( _category, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
        }
        
        Category cat = CategoryHome.findByCode( _category.getCode( ) );
        
        if ( cat != null && cat.getId( ) != _category.getId( ) )
        {
            addError( ERROR_CATEGORY_CODE_ALREADY_EXIST, getLocale( ) );
            return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
        }

        CategoryHome.update( _category );
        addInfo( INFO_CATEGORY_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CATEGORIES );
    }
    
    /**
     * Action to confirm the deletion of the association between the category and the favorite
     * @param request
     * @return XPage
     */
    @Action( ACTION_CONFIRM_REMOVE_ASSOCIATED_FAVORITE )
    public String getConfirmRemoveAssociatedFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ASSOCIATED_FAVORITE ) );
        url.addParameter( PARAMETER_ID_FAVORITE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ASSOCIATED_FAVORITE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal association between favorite and category
     *
     * @param request The Http request
     * @return the jsp URL to display the form to remove association between favorites and categories
     */
    @Action( ACTION_REMOVE_ASSOCIATED_FAVORITE )
    public String doRemoveAssociatedFavorite( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );      
        Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( nId );
        
        if ( favorite != null )
        {
            favorite.setCategoryCode( StringUtils.EMPTY );
            FavoriteService.getInstance( ).update( favorite );
            addInfo( INFO_ASSOCIATION_FAVORITE_REMOVED, getLocale( ) );
        }

        return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
    }
    
    /**
     * Handles the removal association between favorite and category
     *
     * @param request The Http request
     * @return the jsp URL to display the form to remove association between favorites and categories
     */
    @Action( ACTION_ASSOCIATE_FAVORITE_CATEGORY )
    public String doAssociateFavoriteCategory( HttpServletRequest request )
    {
        int nIdFavorite = Integer.parseInt( request.getParameter( PARAMETER_ID_FAVORITE ) );
        
        Favorite favorite = FavoriteService.getInstance( ).findByPrimaryKey( nIdFavorite );
        if ( favorite != null && StringUtils.isEmpty( favorite.getCategoryCode( ) ) )
        {
            favorite.setCategoryCode( _category.getCode( ) );
            FavoriteService.getInstance( ).update( favorite );
            addInfo( INFO_ASSOCIATION_FAVORITE_CREATED, getLocale( ) );
        }
        return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
    }
    
    /**
     * Action to confirm the deletion of the association between the category and demand type
     * @param request
     * @return XPage
     */
    @Action( ACTION_CONFIRM_REMOVE_ASSOCIATED_DEMAND_TYPE )
    public String getConfirmRemoveAssociatedDemandType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND_TYPE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ASSOCIATED_DEMAND_TYPE ) );
        url.addParameter( PARAMETER_ID_DEMAND_TYPE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ASSOCIATED_DEMAND_TYPE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal association between demand type and category
     *
     * @param request The Http request
     * @return the jsp URL to display the form to remove association between demand types and categories
     */
    @Action( ACTION_REMOVE_ASSOCIATED_DEMAND_TYPE )
    public String doRemoveAssociatedDemandType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND_TYPE ) );      

        DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).updateDemandType( nId,  StringUtils.EMPTY );

        addInfo( INFO_ASSOCIATION_DEMAND_TYPE_REMOVED, getLocale( ) );
   
        return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
    }
    
    /**
     * Handles the removal association between demand type and category
     *
     * @param request The Http request
     * @return the jsp URL to display the form to remove association between demand types and categories
     */
    @Action( ACTION_ASSOCIATE_DEMAND_TYPE_CATEGORY )
    public String doAssociateDemandTypeCategory( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND_TYPE ) );
        
        DemandTypeService.getInstance( ).getDemandTypeServiceProvider( ).updateDemandType( nId, _category.getCode( ) );
        addInfo( INFO_ASSOCIATION_DEMAND_TYPE_CREATED, getLocale( ) );

        return redirect( request, VIEW_MODIFY_CATEGORY, PARAMETER_ID_CATEGORY, _category.getId( ) );
    }
    
    /**
     * 
     * @param strId the id of the category
     * Remove all the subscriptions attached to the category
     */
    private void removeAllCategorySubscriptions( String strId )
    {
        SubscriptionFilter sFilter = new SubscriptionFilter( );
        sFilter.setIdSubscribedResource( strId );
        sFilter.setSubscriptionProvider( CategoriesSubscriptionProviderService.getInstance( ).getProviderName( ) );
        List<Subscription> listSubscriptionCategories = SubscriptionService.getInstance( ).findByFilter( sFilter );
        for ( Subscription sub : listSubscriptionCategories )
        {
            SubscriptionService.getInstance( ).removeSubscription( sub, false );
        }
    }
}