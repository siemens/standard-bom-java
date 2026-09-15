/*
 * SPDX-FileCopyrightText: Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom.model;

import java.util.Arrays;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;


/**
 * Organization information as found in the SBOM document.
 */
public class OrganizationInfo
{
    private String name;

    private List<String> urls;



    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")  // preserve modifiability
    public OrganizationInfo(@Nullable final String pName, @Nullable final String pUrl)
    {
        name = pName;
        urls = pUrl != null ? Arrays.asList(pUrl) : null;
    }



    public OrganizationInfo(@Nullable final String pName, @Nullable final List<String> pUrls)
    {
        name = pName;
        urls = pUrls;
    }



    public OrganizationInfo(@Nullable final String pName)
    {
        this(pName, (List<String>) null);
    }



    @CheckForNull
    public String getName()
    {
        return name;
    }



    public void setName(@Nullable final String pName)
    {
        name = pName;
    }



    @CheckForNull
    public List<String> getUrls()
    {
        return urls;
    }



    public void setUrls(@Nullable final List<String> pUrls)
    {
        urls = pUrls;
    }



    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")  // preserve modifiability
    public void setUrl(@Nullable final String pUrl)
    {
        urls = pUrl != null ? Arrays.asList(pUrl) : null;
    }
}
