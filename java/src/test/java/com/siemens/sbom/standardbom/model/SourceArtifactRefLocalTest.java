/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import org.cyclonedx.model.ExternalReference;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests of {@link SourceArtifactRefLocal}.
 */
public class SourceArtifactRefLocalTest
{
    @Test
    public void testSetRelativePathNull()
    {
        SourceArtifactRefLocal underTest = new SourceArtifactRefLocal();
        underTest.setRelativePath(null);

        final ExternalReference actual = underTest.getCycloneDxRef();
        Assert.assertEquals(ExternalReference.Type.DISTRIBUTION, actual.getType());
        Assert.assertEquals(SourceArtifactRefLocal.SOURCE_ARCHIVE_LOCAL, actual.getComment());
        Assert.assertEquals("file:null", actual.getUrl());
    }



    @Test
    public void testNullNotValid()
    {
        Assert.assertFalse(SourceArtifactRefLocal.isSourceReference(null));
    }



    @Test
    public void testNoFilePath()
    {
        SourceArtifactRefLocal underTest = new SourceArtifactRefLocal();
        underTest.getCycloneDxRef().setUrl("https://example.com");

        Assert.assertEquals("https://example.com", underTest.getRelativePath());
    }



    @Test
    public void testNullNoFilePath()
    {
        SourceArtifactRefLocal underTest = new SourceArtifactRefLocal();
        underTest.getCycloneDxRef().setUrl(null);

        Assert.assertNull(underTest.getRelativePath());
    }
}
