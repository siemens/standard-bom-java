/*
 * SPDX-FileCopyrightText: Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link OrganizationInfo}.
 */
public class OrganizationInfoTest
{
    @Test
    public void testNameConstructor()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg");
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertNull(underTest.getUrls());
    }



    @Test
    public void testNameConstructorNull()
    {
        OrganizationInfo underTest = new OrganizationInfo(null);
        Assert.assertNull(underTest.getName());
        Assert.assertNull(underTest.getUrls());
    }



    @Test
    public void testUrlConstructor()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", "https://example.com");
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertEquals(Collections.singletonList("https://example.com"), underTest.getUrls());
    }



    @Test
    public void testUrlConstructorNullUrl()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", (String) null);
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertNull(underTest.getUrls());
    }



    @Test
    public void testUrlConstructorNullName()
    {
        OrganizationInfo underTest = new OrganizationInfo(null, "https://example.com");
        Assert.assertNull(underTest.getName());
        Assert.assertEquals(Collections.singletonList("https://example.com"), underTest.getUrls());
    }



    @Test
    public void testUrlsConstructor()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg",
            Arrays.asList("https://example.com", "https://opensource.example.com"));
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertEquals(Arrays.asList("https://example.com", "https://opensource.example.com"),
            underTest.getUrls());
    }



    @Test
    public void testUrlsConstructorNullUrls()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", (List<String>) null);
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertNull(underTest.getUrls());
    }



    @Test
    public void testUrlsConstructorEmptyUrls()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", Collections.emptyList());
        Assert.assertEquals("SomeOrg", underTest.getName());
        Assert.assertEquals(Collections.emptyList(), underTest.getUrls());
    }



    @Test
    public void testUrlsConstructorNullName()
    {
        OrganizationInfo underTest = new OrganizationInfo(null,
            Arrays.asList("https://example.com", "https://opensource.example.com"));
        Assert.assertNull(underTest.getName());
        Assert.assertEquals(Arrays.asList("https://example.com", "https://opensource.example.com"),
            underTest.getUrls());
    }



    @Test
    public void testSetName()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", "https://example.com");
        underTest.setName("SomeOrg AG");
        Assert.assertEquals("SomeOrg AG", underTest.getName());
        Assert.assertEquals(Collections.singletonList("https://example.com"), underTest.getUrls());

        underTest.setName(null);
        Assert.assertNull(underTest.getName());
        Assert.assertEquals(Collections.singletonList("https://example.com"), underTest.getUrls());
    }



    @Test
    public void testSetUrls()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", "https://example.com");
        underTest.setUrls(Arrays.asList("https://opensource.example.com", "https://sbom.example.com"));
        Assert.assertEquals(Arrays.asList("https://opensource.example.com", "https://sbom.example.com"),
            underTest.getUrls());
        Assert.assertEquals("SomeOrg", underTest.getName());

        underTest.setUrls(Collections.emptyList());
        Assert.assertEquals(Collections.emptyList(), underTest.getUrls());
        Assert.assertEquals("SomeOrg", underTest.getName());

        underTest.setUrls(null);
        Assert.assertNull(underTest.getUrls());
        Assert.assertEquals("SomeOrg", underTest.getName());
    }



    @Test
    public void testSetUrl()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg",
            Arrays.asList("https://example.com", "https://opensource.example.com"));
        underTest.setUrl("https://another.example.com");
        Assert.assertEquals(Collections.singletonList("https://another.example.com"), underTest.getUrls());
        Assert.assertEquals("SomeOrg", underTest.getName());

        underTest.setUrl(null);
        Assert.assertNull(underTest.getUrls());
        Assert.assertEquals("SomeOrg", underTest.getName());
    }



    @Test
    public void testUrlConstructorAllowsElementReplacement()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg", "https://example.com");
        List<String> urls = underTest.getUrls();
        Assert.assertNotNull(urls);
        urls.set(0, "https://opensource.example.com");
        Assert.assertEquals(Collections.singletonList("https://opensource.example.com"), underTest.getUrls());
    }



    @Test
    public void testSetUrlAllowsElementReplacement()
    {
        OrganizationInfo underTest = new OrganizationInfo("SomeOrg");
        underTest.setUrl("https://example.com");
        List<String> urls = underTest.getUrls();
        Assert.assertNotNull(urls);
        urls.set(0, "https://opensource.example.com");
        Assert.assertEquals(Collections.singletonList("https://opensource.example.com"), underTest.getUrls());
    }
}
