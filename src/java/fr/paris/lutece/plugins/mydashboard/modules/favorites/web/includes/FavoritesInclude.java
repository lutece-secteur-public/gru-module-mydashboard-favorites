package fr.paris.lutece.plugins.mydashboard.modules.favorites.web.includes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mydashboard.modules.favorites.business.Category;
import fr.paris.lutece.plugins.mydashboard.modules.favorites.service.FavoriteService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;

public class FavoritesInclude implements PageInclude
{
    private static final String MARK_CATEGORY_LIST = "categoryList";
    
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        if ( request != null )
        {
            List<Category> categoryList = FavoriteService.getInstance( ).findAllCategories( );
            
            rootModel.put( MARK_CATEGORY_LIST, categoryList );
        }
    }
}