/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import org.cyclonedx.model.ExternalReference;
import org.junit.Assert;
import org.junit.Test;


/**
 * Some unit tests of {@link SourceArtifactRef}.
 */
public class SourceArtifactRefTest
{
    @Test
    public void testHashAccessors()
    {
        final SourceArtifactRef underTest = new SourceArtifactRef();
        underTest.setMd5("feae16743dc96025839c3a296392473d");
        underTest.setSha1("338d2678d341eedbc5273f983766dc65ffed13c2");
        underTest.setSha256("4dde81a4a934d61f3ae9f92d896facbf76a55da410f70c0ebdf4f3c0778c8d78");
        underTest.setSha512("46a80cb319320f7c2d5b173cb3f4c77f1d312c0b12066fcc1282d001c63a5fac"
            + "7d0ca5f192d562969f3a8cbdccada3f6b9fdf602ca9985a7e81e970fdbf58aa7");

        Assert.assertEquals("feae16743dc96025839c3a296392473d", underTest.getMd5());
        Assert.assertEquals("338d2678d341eedbc5273f983766dc65ffed13c2", underTest.getSha1());
        Assert.assertEquals("4dde81a4a934d61f3ae9f92d896facbf76a55da410f70c0ebdf4f3c0778c8d78", underTest.getSha256());
        Assert.assertEquals("46a80cb319320f7c2d5b173cb3f4c77f1d312c0b12066fcc1282d001c63a5fac"
            + "7d0ca5f192d562969f3a8cbdccada3f6b9fdf602ca9985a7e81e970fdbf58aa7", underTest.getSha512());
    }



    @Test
    public void testNull()
    {
        Assert.assertFalse(SourceArtifactRef.isSourceReference(null));
    }



    @Test
    public void testNullComment()
    {
        final ExternalReference ref = new ExternalReference();
        ref.setType(ExternalReference.Type.DISTRIBUTION);
        ref.setUrl("https://example.com");
        ref.setComment(null);

        Assert.assertFalse(SourceArtifactRef.isSourceReference(ref));
    }
}
