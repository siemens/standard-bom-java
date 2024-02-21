/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cyclonedx.model.Hash;
import org.junit.Assert;
import org.junit.Test;


/**
 * Some unit tests of {@link HashProcessor}.
 */
public class HashProcessorTest
{
    @Test
    public void testUnknownAlgorithm()
    {
        final AtomicBoolean called = new AtomicBoolean(false);
        HashProcessor underTest = new HashProcessor(ArrayList::new, hash -> {
            called.set(true);
            Assert.assertEquals(Hash.Algorithm.BLAKE3.getSpec(), hash.getAlgorithm());
            Assert.assertEquals("some test hash (unvalidated)", hash.getValue());
        });
        underTest.set(Hash.Algorithm.BLAKE3, "some test hash (unvalidated)");
        Assert.assertTrue(called.get());
    }
}
