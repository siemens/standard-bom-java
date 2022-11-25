/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import org.cyclonedx.exception.ParseException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.siemens.sbom.standardbom.internal.VersionUtil;
import com.siemens.sbom.standardbom.model.BomEntry;
import com.siemens.sbom.standardbom.model.StandardBom;


/**
 * Unit tests for {@link StandardBomParser}.
 */
public class StandardBomParserTest
{
    private static final String CURRENT_BOM_VERSION = VersionUtil.getFormatVersion();

    @Rule
    public final TemporaryFolder tempDir = new TemporaryFolder();



    @Test(expected = FileNotFoundException.class)
    public void testReadFileNotFound()
        throws IOException, ParseException
    {
        new StandardBomParser().parse(new File("NON-EXISTENT.FILE"));
        Assert.fail("Expected exception was not thrown");
    }



    @Test
    public void testReadSunnyDay()
        throws IOException, ParseException
    {
        StandardBom parsed = parseFile("full-valid.json");

        Assert.assertNotNull(parsed);
        Assert.assertNotNull(parsed.getMetadata());
        Assert.assertNotNull(parsed.getMetadata().getTools());
        Assert.assertEquals(3, parsed.getMetadata().getTools().size());
        Assert.assertEquals(new Date(1657292400000L), parsed.getMetadata().getTimestamp());
        Assert.assertEquals(9, parsed.getComponents().size());
        Assert.assertNotNull(parsed.getCycloneDxBom().getExternalReferences());
        Assert.assertEquals(1, parsed.getCycloneDxBom().getExternalReferences().size());
    }



    @Test
    public void testReadSunnyDayViaFile()
        throws IOException, URISyntaxException, ParseException
    {
        @SuppressWarnings("ConstantConditions")
        final File jsonFile = new File(getClass().getResource("full-valid.json").toURI());

        final StandardBom parsed = new StandardBomParser().parse(jsonFile);

        Assert.assertNotNull(parsed);
        Assert.assertNotNull(parsed.getMetadata());
        Assert.assertNotNull(parsed.getMetadata().getTools());
        Assert.assertEquals(3, parsed.getMetadata().getTools().size());
        Assert.assertEquals(new Date(1657292400000L), parsed.getMetadata().getTimestamp());
        Assert.assertEquals(9, parsed.getComponents().size());
        Assert.assertNotNull(parsed.getCycloneDxBom().getExternalReferences());
        Assert.assertEquals(1, parsed.getCycloneDxBom().getExternalReferences().size());
    }



    @Test
    public void testWriteToFileSunnyDay()
        throws IOException, URISyntaxException, ParseException
    {
        @SuppressWarnings("ConstantConditions")
        byte[] expected = Files.readAllBytes(Paths.get(getClass().getResource("full-valid.json").toURI()));
        String expectedStr = new String(expected, StandardCharsets.UTF_8);
        expectedStr = expectedStr.replaceAll(Pattern.quote("${currentVersion}"),
            Matcher.quoteReplacement(CURRENT_BOM_VERSION));
        expected = expectedStr.getBytes(StandardCharsets.UTF_8);

        StandardBom parsed = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(expected)) {
            parsed = new StandardBomParser().parse(bais);
        }

        File outputFile = new File(tempDir.getRoot(), "test-output.json");
        new StandardBomParser().save(parsed, outputFile);
        final byte[] actual = Files.readAllBytes(Paths.get(outputFile.toURI()));

        Assert.assertArrayEquals(expected, actual);
    }



    /**
     * Test object that simulates an exception while trying to serialize it as JSON.
     */
    private static class BrokenBom
        extends StandardBom
    {
        public BrokenBom()
        {
            super();
        }



        @Nonnull
        @Override
        public List<BomEntry> getComponents()
        {
            throw new IllegalStateException("THROWN FOR TESTING");
        }
    }



    @Test(expected = StandardBomException.class)
    public void testWriteJsonFailed()
        throws IOException
    {
        File outputFile = new File(tempDir.getRoot(), "test-output2.json");
        new StandardBomParser().save(new BrokenBom(), outputFile);
        Assert.fail("Expected exception was not thrown");
    }



    @Test(expected = IOException.class)
    public void testWritePermissionDenied()
        throws IOException
    {
        Assume.assumeTrue(System.getProperty("os.name").contains("Windows"));  // test only works on Windows :-/

        final File outputFile = new File(tempDir.getRoot(), "permission-denied.json");
        Assert.assertTrue(outputFile.createNewFile());
        Assert.assertTrue(outputFile.setReadOnly());
        new StandardBomParser().save(new StandardBom(), outputFile);
        Assert.fail("Expected exception was not thrown");
    }



    @SuppressWarnings("SameParameterValue")
    private StandardBom parseFile(final String pFilename)
        throws IOException, ParseException
    {
        try (InputStream fis = getClass().getResourceAsStream(pFilename)) {
            if (fis != null) {
                return new StandardBomParser().parse(fis);
            }
        }
        return null;
    }
}
