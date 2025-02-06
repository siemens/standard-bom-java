/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.junit.Assert;
import org.junit.Test;


/**
 * Trivial unit tests for {@link ExternalComponent}.
 */
public class ExternalComponentTest
{
    /**
     * Make sure that all getters and setters are present as expected.
     */
    @Test
    public void testAccessors()
    {
        ExternalComponent underTest = new ExternalComponent();

        underTest.setExternalId("41983931");
        underTest.setType(Component.Type.LIBRARY);
        underTest.setDescription("description text");
        underTest.setLegalRemark("legal remark");
        underTest.setUrl(null);

        Assert.assertEquals(ExternalReference.Type.OTHER, underTest.getCycloneDxRef().getType());
        Assert.assertEquals("41983931", underTest.getExternalId());
        Assert.assertEquals(Component.Type.LIBRARY, underTest.getType());
        Assert.assertEquals("description text", underTest.getDescription());
        Assert.assertEquals("legal remark", underTest.getLegalRemark());
        Assert.assertNull(underTest.getUrl());
    }



    /**
     * Make sure that deprecated getters and setters are also present as expected.
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedAccessors()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setPurl("pkg:maven/org.yaml/snakeyaml@1.23");

        Assert.assertEquals(ExternalReference.Type.OTHER, underTest.getCycloneDxRef().getType());
        Assert.assertEquals("pkg:maven/org.yaml/snakeyaml@1.23", underTest.getPurl());
        Assert.assertNull(underTest.getExternalId());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBlackduckComponentId()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setExternalId("INVALID", ExternalComponent.BLACKDUCK_COMPONENT_ID_PATTERN);
        Assert.fail("expected exception was not thrown");
    }



    @Test
    public void testValidSw360Id()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setExternalId("f9159fde78553c2ba192b7fa8e8c2033", ExternalComponent.SW360_ID_PATTERN);
        Assert.assertEquals("f9159fde78553c2ba192b7fa8e8c2033", underTest.getExternalId());
    }



    @Test
    public void testNullBlackduckComponentId()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setExternalId(null, ExternalComponent.BLACKDUCK_COMPONENT_ID_PATTERN);
        Assert.assertNull(underTest.getExternalId());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPurl()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setUrl("INVALID");
        Assert.fail("expected exception was not thrown");
    }



    @Test
    public void testLegalRemark()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setLegalRemark("test remark");
        Assert.assertEquals("test remark", underTest.getLegalRemark());
        Assert.assertEquals(ExternalComponent.UNKNOWN_PURL, underTest.getUrl());
    }



    @Test
    public void testTypeNull()
    {
        ExternalComponent underTest = new ExternalComponent();
        underTest.setType(null);
        Assert.assertNull(underTest.getType());
    }



    @Test
    public void testIsExternalComponentNull()
    {
        Assert.assertFalse(ExternalComponent.isExternalComponent(null));
    }



    @Test
    public void testIsExternalComponentNoUrl()
    {
        ExternalReference ref = new ExternalReference();
        ref.setType(ExternalReference.Type.OTHER);
        ref.setUrl(null);
        Assert.assertFalse(ExternalComponent.isExternalComponent(ref));
    }



    @Test
    public void testIsExternalComponentNoPurl()
    {
        ExternalReference ref = new ExternalReference();
        ref.setType(ExternalReference.Type.OTHER);
        ref.setUrl("https://not-a-purl.com");
        Assert.assertFalse(ExternalComponent.isExternalComponent(ref));
    }



    @Test
    public void testIsExternalComponentNoProperties()
    {
        ExternalReference ref = new ExternalReference();
        ref.setType(ExternalReference.Type.OTHER);
        ref.setUrl("pkg:maven/org.yaml/snakeyaml@1.23");
        ref.setComment("\\uINVALID");
        Assert.assertFalse(ExternalComponent.isExternalComponent(ref));
    }
}
