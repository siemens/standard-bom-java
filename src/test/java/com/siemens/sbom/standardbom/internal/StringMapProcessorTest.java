/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests of {@link StringMapProcessor}.
 */
public class StringMapProcessorTest
{
    @Test
    public void testSimple()
    {
        String[] actual = new String[]{null};
        StringMapProcessor underTest = new StringMapProcessor(() -> actual[0], s -> actual[0] = s);

        underTest.set("foo", "bar");
        underTest.set("baz", "bar");
        underTest.set("bar", "boo");

        Assert.assertEquals("bar=boo\nbaz=bar\nfoo=bar", actual[0]);
    }



    @Test
    public void testMultipleOps()
    {
        String[] actual = new String[]{null};
        StringMapProcessor underTest = new StringMapProcessor(() -> actual[0], s -> actual[0] = s);

        underTest.set("foo", "bar");
        underTest.set("baz", "bar");
        underTest.set("bar", "boo");
        underTest.set("baz", null);
        underTest.set("foo", "  ");

        Assert.assertEquals("bar=boo", actual[0]);
    }



    @Test
    public void testEmptyAgain()
    {
        String[] actual = new String[]{null};
        StringMapProcessor underTest = new StringMapProcessor(() -> actual[0], s -> actual[0] = s);

        underTest.set("foo", "bar");
        underTest.set("foo", null);

        Assert.assertNull(actual[0]);
    }



    @Test
    public void testEscapedRead()
    {
        String[] actual = new String[]{"bar=boo \\=> baz\nfoo=line1\\nline2"};
        StringMapProcessor underTest = new StringMapProcessor(() -> actual[0], s -> actual[0] = s);

        Assert.assertEquals("line1\nline2", underTest.get("foo"));
        Assert.assertEquals("boo => baz", underTest.get("bar"));
    }



    @Test
    public void testEscapedWrite()
    {
        String[] actual = new String[]{null};
        StringMapProcessor underTest = new StringMapProcessor(() -> actual[0], s -> actual[0] = s);

        underTest.set("foo", "line1\nline2");
        underTest.set("bar", "boo => baz");

        Assert.assertEquals("bar=boo \\=> baz\nfoo=line1\\nline2", actual[0]);
    }



    @Test
    public void testUnableToDeserialize()
        throws IOException
    {
        byte[] bytes = null;
        try (InputStream is = getClass().getResourceAsStream("malformed.properties")) {
            Assert.assertNotNull(is);
            bytes = IOUtils.toByteArray(is);
        }
        final String broken = new String(bytes, StandardCharsets.UTF_8);

        StringMapProcessor underTest = new StringMapProcessor(() -> broken,
            s -> Assert.fail("expected exception was not processed"));

        Assert.assertNull(underTest.get("broken"));
    }
}
