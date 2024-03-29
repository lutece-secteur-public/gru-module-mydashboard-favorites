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

import javax.validation.constraints.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.*;
import java.io.Serializable;

/**
 * This is the business class for the object Favorite
 */ 
public class Favorite implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{module.mydashboard.favorites.validation.favorite.Label.notEmpty}" )
    @Size( max = 255 , message = "#i18n{module.mydashboard.favorites.validation.favorite.Label.size}" ) 
    private String _strLabel;
    @URL(message = "#i18n{portal.validation.message.url}")
    @NotEmpty( message = "#i18n{module.mydashboard.favorites.validation.favorite.Url.notEmpty}" )
    @Size( max = 255 , message = "#i18n{module.mydashboard.favorites.validation.favorite.Url.size}" ) 
    private String _strUrl;
    
    private boolean _bIsActivated;
    
    private String _strProviderName;
    
    private String _strRemoteId;
    
    private boolean _bIsDefault;
    
    private int _nOrder;
    
    private String _strDescription;
    
    private String _strPictogramme;
    
    private String _strCategoryCode;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the Label
     * @return The Label
     */
    public String getLabel( )
    {
        return _strLabel;
    }

    /**
     * Sets the Label
     * @param strLabel The Label
     */ 
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }
    
    /**
     * Returns the Url
     * @return The Url
     */
    public String getUrl( )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     * @param strUrl The Url
     */ 
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }
    
    /**
     * Returns the IsActivated
     * @return The IsActivated
     */
    public boolean getIsActivated( )
    {
        return _bIsActivated;
    }

    /**
     * Sets the IsActivated
     * @param bIsActivated The IsActivated
     */ 
    public void setIsActivated( boolean bIsActivated )
    {
        _bIsActivated = bIsActivated;
    }

    /**
     * Returns the Provider Name
     * @return The Provider Name
     */
    public String getProviderName( )
    {
        return _strProviderName;
    }

    /**
     * Sets the Provider Name
     * @param strProviderName The ProviderName
     */
    public void setProviderName( String strProviderName )
    {
        this._strProviderName = strProviderName;
    }

    /**
     * Returns the remote Id of the favorite
     * @return The Remote Id of the favorite
     */
    public String getRemoteId( )
    {
        return _strRemoteId;
    }
    
    /**
     * Set the remote Id of the favorite
     * @param strRemoteId the remote Id of the favorite
     */
    public void setRemoteId( String strRemoteId )
    {
        this._strRemoteId = strRemoteId;
    }

    /**
     * Return the default boolean
     * @return true if default, false otherwise
     */
    public boolean getIsDefault()
    {
        return _bIsDefault;
    }

    /**
     * Set the default boolean
     * @param bIsDefault 
     */
    public void setIsDefault( boolean bIsDefault )
    {
        _bIsDefault = bIsDefault;
    }

    /**
     * @return the _nOrder
     */
    public int getOrder( )
    {
        return _nOrder;
    }

    /**
     * @param nOrder the _nOrder to set
     */
    public void setOrder( int nOrder )
    {
        this._nOrder = nOrder;
    }

    /**
     * @return the _strDescription
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * @param strDescription the _strDescription to set
     */
    public void setDescription( String strDescription )
    {
        this._strDescription = strDescription;
    }

    /**
     * @return the _strPictogramme
     */
    public String getPictogramme( )
    {
        return _strPictogramme;
    }

    /**
     * @param strPictogramme the _strPictogramme to set
     */
    public void setPictogramme( String strPictogramme )
    {
        this._strPictogramme = strPictogramme;
    }
    
    /**
     * @return the _strCategoryCode
     */
    public String getCategoryCode( )
    {
        return _strCategoryCode;
    }

    /**
     * @param strPictogramme the _strCategoryCode to set
     */
    public void setCategoryCode( String strCategoryCode )
    {
        this._strCategoryCode = strCategoryCode;
    }

}
