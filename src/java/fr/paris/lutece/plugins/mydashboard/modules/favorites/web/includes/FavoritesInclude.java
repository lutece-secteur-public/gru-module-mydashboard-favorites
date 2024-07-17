package fr.paris.lutece.plugins.mydashboard.modules.favorites.web.includes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Category;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.CategoryHome;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.CategoriesSubscriptionProviderService;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class FavoritesInclude implements PageInclude
{
    private static final String MARK_CATEGORY_LIST = "categoryList";
    
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        if ( request != null )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            
            if ( user != null )
            {
                SubscriptionFilter sFilter = new SubscriptionFilter( );
                sFilter.setIdSubscriber( user.getName( ) );
                sFilter.setSubscriptionProvider( CategoriesSubscriptionProviderService.getInstance( ).getProviderName( ) );
                List<Subscription> listCategories = SubscriptionService.getInstance( ).findByFilter( sFilter );
                //get favorite categories about those subscriptions
                List<Category> listCategoriesSuscribed = new ArrayList<Category>( );
                for ( Subscription sub : listCategories )
                {
                    try
                    {
                        int idCategory = Integer.parseInt( sub.getIdSubscribedResource( ) );
                        Category category = CategoryHome.findByPrimaryKey( idCategory );
                        if ( category != null )
                        {
                            listCategoriesSuscribed.add( category );
                        }
                    }
                    catch ( NumberFormatException e )
                    {
                        AppLogService.error( "Error with category id format for category with id: " + sub.getIdSubscribedResource( ), e );
                    }
                }
                
                rootModel.put( MARK_CATEGORY_LIST, listCategoriesSuscribed );
            }
        }
    }
}