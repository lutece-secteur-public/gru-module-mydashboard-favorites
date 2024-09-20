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
package fr.paris.lutece.plugins.mydashboard.modules.favorites.service.provider;

import java.util.List;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * 
 * DemandTypeService
 *
 */
public class DemandTypeService
{
    
    private IDemandTypeProvider _demandTypeProvider;
    private static DemandTypeService _instance;
    
    // messages
    private static final String MSG_NO_DEMAND_TYPE_SERVICE = "No demand type service Available";
    
    /**
     * Private constuctor 
     */
    private DemandTypeService( )
    {
        _demandTypeProvider = getDefaultServiceProvider( );
    }
    
    /**
     * Getter instance
     * @return instance
     */
    public static DemandTypeService getInstance( )
    {
        if( _instance == null )
        {
            _instance = new DemandTypeService( );
        }
        return _instance;
    }
    
    
    /**
     * get the DemandTypeService provider
     * 
     * @param strDemandTypeServiceProviderName
     * @return the current DemandTypeService provider
     */
    public IDemandTypeProvider getDemandTypeServiceProvider( )
    {
        return _demandTypeProvider;
    }
    
    /**
     * get the DemandTypeService provider
     * 
     * @param strDemandTypeServiceProviderName
     * @return the current DemandTypeService provider
     */
    public IDemandTypeProvider getDemandTypeServiceProvider( String strDemandTypeServiceProviderName )
    {
        List<IDemandTypeProvider> demandTypeServiceProviderList = SpringContextService.getBeansOfType( IDemandTypeProvider.class );

        // search demand type service
        if ( !demandTypeServiceProviderList.isEmpty( ) )
        {
            for ( IDemandTypeProvider dtp : demandTypeServiceProviderList )
            {
                if ( strDemandTypeServiceProviderName.equals( dtp.getName( ) ) )
                {
                      return dtp;
                }
            }
        }

        // otherwise
        throw new AppException( MSG_NO_DEMAND_TYPE_SERVICE );
    }
    
    /**
     * get default Demand type Service Provider
     * 
     * @return the provider
     */
    private IDemandTypeProvider getDefaultServiceProvider( )
    {
        List<IDemandTypeProvider> demandTypeServiceProviderList = SpringContextService.getBeansOfType( IDemandTypeProvider.class );

        // search default demand type service
        if ( !demandTypeServiceProviderList.isEmpty( ) )
        {
            for ( IDemandTypeProvider dtp : demandTypeServiceProviderList )
            {
                if ( dtp.isDefault( ) )
                {
                    return dtp;
                }
            }
        }
        
        // otherwise
        throw new AppException( MSG_NO_DEMAND_TYPE_SERVICE );
    }
}
