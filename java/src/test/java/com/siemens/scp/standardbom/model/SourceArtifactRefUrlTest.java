/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.scp.standardbom.model;

import org.junit.Assert;
import org.junit.Test;


/**
 * Some unit tests of {@link SourceArtifactRefUrl}.
 */
public class SourceArtifactRefUrlTest
{
    @Test
    public void testNullNotValid()
    {
        Assert.assertFalse(SourceArtifactRefUrl.isSourceReference(null));
    }
}
