/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom.model;

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
