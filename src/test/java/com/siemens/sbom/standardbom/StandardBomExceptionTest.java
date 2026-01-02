/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests of {@link StandardBomException}.
 */
public class StandardBomExceptionTest
{
    @Test
    public void testIsExternalComponentNull()
    {
        StandardBomException underTest = new StandardBomException("msg");
        Assert.assertEquals("msg", underTest.getMessage());
    }
}
