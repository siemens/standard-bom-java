/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import com.siemens.sbom.standardbom.StandardBomException;


/**
 * A few unit tests for {@link VersionUtil}.
 */
public class VersionUtilTest
{
    @Test
    public void testFileNotFound()
    {
        try {
            VersionUtil.readProperties("NON-EXISTENT.FILE");
            Assert.fail("Expected exception was not thrown");
        }
        catch (StandardBomException e) {
            // expected
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals(FileNotFoundException.class, e.getCause().getClass());
        }
    }



    @Test
    public void testBrokenFile()
    {
        try {
            VersionUtil.readProperties("malformed.properties");
            Assert.fail("Expected exception was not thrown");
        }
        catch (StandardBomException e) {
            // expected
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }
}
