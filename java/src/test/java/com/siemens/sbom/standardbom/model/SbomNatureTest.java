/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests of {@link SbomNature}.
 */
public class SbomNatureTest
{
    @Test
    public void testParseNature()
    {
        SbomNature actual = SbomNature.parseNature("binary");
        Assert.assertEquals(SbomNature.Binary, actual);

        actual = SbomNature.parseNature("SOURCe");
        Assert.assertEquals(SbomNature.Source, actual);
    }



    @Test
    public void testParseNatureNull()
    {
        SbomNature actual = SbomNature.parseNature(null);
        Assert.assertNull(actual);
    }



    @Test
    public void testParseNatureInvalid()
    {
        SbomNature actual = SbomNature.parseNature("INVALID");
        Assert.assertNull(actual);
    }
}
