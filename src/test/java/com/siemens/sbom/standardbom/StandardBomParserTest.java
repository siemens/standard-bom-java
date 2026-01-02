/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.License;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.siemens.sbom.standardbom.internal.VersionUtil;
import com.siemens.sbom.standardbom.model.BomEntry;
import com.siemens.sbom.standardbom.model.SbomNature;
import com.siemens.sbom.standardbom.model.SourceArtifactRefLocal;
import com.siemens.sbom.standardbom.model.SourceArtifactRefUrl;
import com.siemens.sbom.standardbom.model.StandardBom;


/**
 * Unit tests for {@link StandardBomParser}.
 */
public class StandardBomParserTest
{
    private static final String LIBRARY_VERSION = VersionUtil.getLibraryVersion();

    private static final String FORMAT_VERSION = VersionUtil.getSpecVersion();

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
    public void testReadSunnyDay16()
        throws IOException, ParseException
    {
        StandardBom parsed = parseFile("full-valid.cdx.json");

        Assert.assertNotNull(parsed);
        Assert.assertNotNull(parsed.getMetadata());
        Assert.assertNotNull(parsed.getMetadata().getToolChoice());
        Assert.assertNotNull(parsed.getMetadata().getToolChoice().getComponents());
        Assert.assertEquals(3, parsed.getMetadata().getToolChoice().getComponents().size());
        Assert.assertEquals(new Date(1657292400000L), parsed.getMetadata().getTimestamp());
        Assert.assertEquals(9, parsed.getComponents().size());
        Assert.assertNotNull(parsed.getCycloneDxBom().getExternalReferences());
        Assert.assertEquals(1, parsed.getCycloneDxBom().getExternalReferences().size());
        Assert.assertEquals("${formatVersion}", parsed.getStandardBomVersion());
    }



    @Test
    public void testReadSunnyDay14()
        throws IOException, ParseException
    {
        StandardBom parsed = parseFile("full-valid-1.4.cdx.json");

        Assert.assertNotNull(parsed);
        Assert.assertNotNull(parsed.getMetadata());
        Assert.assertNotNull(parsed.getMetadata().getTools());
        Assert.assertEquals(4, parsed.getMetadata().getTools().size());
        Assert.assertEquals(new Date(1657292400000L), parsed.getMetadata().getTimestamp());
        Assert.assertEquals(9, parsed.getComponents().size());
        Assert.assertNotNull(parsed.getCycloneDxBom().getExternalReferences());
        Assert.assertEquals(1, parsed.getCycloneDxBom().getExternalReferences().size());
        Assert.assertEquals("${formatVersion}", parsed.getStandardBomVersion());
    }



    @Test
    public void testReadSunnyDayViaFile()
        throws IOException, URISyntaxException, ParseException
    {
        @SuppressWarnings("ConstantConditions")
        final File jsonFile = new File(getClass().getResource("full-valid.cdx.json").toURI());

        final StandardBom parsed = new StandardBomParser().parse(jsonFile);

        Assert.assertNotNull(parsed);
        Assert.assertNotNull(parsed.getMetadata());
        Assert.assertNotNull(parsed.getMetadata().getToolChoice());
        Assert.assertNotNull(parsed.getMetadata().getToolChoice().getComponents());
        Assert.assertEquals(3, parsed.getMetadata().getToolChoice().getComponents().size());
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
        byte[] expected = Files.readAllBytes(Paths.get(getClass().getResource("full-valid.cdx.json").toURI()));
        String expectedStr = new String(expected, StandardCharsets.UTF_8);
        expectedStr = expectedStr.replaceAll(Pattern.quote("${libraryVersion}"),
            Matcher.quoteReplacement(LIBRARY_VERSION));
        expectedStr = expectedStr.replaceAll(Pattern.quote("${formatVersion}"),
            Matcher.quoteReplacement(FORMAT_VERSION));
        expected = expectedStr.getBytes(StandardCharsets.UTF_8);

        StandardBom parsed = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(expected)) {
            parsed = new StandardBomParser().parse(bais);
        }

        File outputFile = new File(tempDir.getRoot(), "test-output.cdx.json");
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
        File outputFile = new File(tempDir.getRoot(), "test-output2.cdx.json");
        new StandardBomParser().save(new BrokenBom(), outputFile);
        Assert.fail("Expected exception was not thrown");
    }



    @Test(expected = IOException.class)
    public void testWritePermissionDenied()
        throws IOException
    {
        Assume.assumeTrue(System.getProperty("os.name").contains("Windows"));  // test only works on Windows :-/

        final File outputFile = new File(tempDir.getRoot(), "permission-denied.cdx.json");
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



    @Test
    public void testCreateSbomFromScratch()
        throws MalformedPackageURLException, IOException, URISyntaxException
    {
        final StandardBom sbom = new StandardBom();
        sbom.setSbomNature(SbomNature.Binary);
        sbom.setProfile("clearing");
        LocalDateTime sometime = LocalDate.of(2026, Month.JANUARY, 2).atTime(LocalTime.of(12, 0, 0));
        sbom.setTimestamp(Date.from(sometime.toInstant(ZoneOffset.UTC)));
        sbom.setSerialNumber("urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79");

        OrganizationalEntity supplier = new OrganizationalEntity();
        supplier.setName("ACME");
        sbom.getMetadata().setSupplier(supplier);

        Component primaryComponent = new Component();
        primaryComponent.setName("primary");
        primaryComponent.setDescription("Description of the primary component");
        primaryComponent.setVersion("2.0.0");
        primaryComponent.setGroup("com.example");
        sbom.getMetadata().setComponent(primaryComponent);

        // actual content:
        sbom.addComponent(buildComponentEntry());

        final File outputFile = tempDir.newFile("scratch-actual.cdx.json");
        new StandardBomParser().save(sbom, outputFile);
        Assert.assertTrue(outputFile.canRead());

        @SuppressWarnings("ConstantConditions")
        byte[] expBytes = Files.readAllBytes(Paths.get(getClass().getResource("scratch-expected.cdx.json").toURI()));
        String expected = new String(expBytes, StandardCharsets.UTF_8);
        expected = expected.replaceAll(Pattern.quote("${libraryVersion}"), Matcher.quoteReplacement(LIBRARY_VERSION));
        expected = expected.replaceAll(Pattern.quote("${formatVersion}"), Matcher.quoteReplacement(FORMAT_VERSION));
        final String actual = new String(Files.readAllBytes(Paths.get(outputFile.toURI())), StandardCharsets.UTF_8);

        Assert.assertEquals(expected, actual);
    }



    private BomEntry buildComponentEntry()
        throws MalformedPackageURLException
    {
        final BomEntry component = new BomEntry();
        component.setType(Component.Type.LIBRARY);
        component.setGroup("com.siemens.sbom");
        component.setName("component1");
        component.setVersion("1.0.0");

        component.setPurl(PackageURLBuilder.aPackageURL()
            .withType(PackageURL.StandardTypes.MAVEN)
            .withNamespace("com.siemens.sbom")
            .withName("component1")
            .withVersion("1.0.0")
            .build().toString());
        component.setBomRef(component.getPurl());

        OrganizationalContact author1 = new OrganizationalContact();
        author1.setName("author1");
        author1.setEmail("author1@example.com");
        OrganizationalContact author2 = new OrganizationalContact();
        author2.setName("author2");
        author2.setEmail("author2@example.com");
        component.setAuthors(Arrays.asList(author1, author2));

        component.setCopyright("(c) the copyright holders");
        component.setThirdPartyNotices("line1\nline2\nline3");
        component.setDescription("This is the description of component1");
        component.setDirectDependency(true);
        component.setCpe("cpe:2.3:a:com.siemens.sbom:component1:-:*:*:*:*:*:*:*");
        component.setFilename("component1.jar");
        component.setLegalRemark("a legal remark");
        component.setPrimaryLanguage("Java");
        component.setInternal(false);
        component.setWebsite("https://example.com");
        component.setRepoUrl("https://example.com/component1.git");
        component.setRelativePath("file:///binaries/d771af8e336e372fb5399c99edabe0919aeaf5b2/component1.jar");
        component.setMd5("15f41a7cddbf60f741bc49966c38f75b");
        component.setSha1("d771af8e336e372fb5399c99edabe0919aeaf5b2");

        License license1 = new License();
        license1.setName("The Apache Software License, version 2.0");
        license1.setUrl("https://www.apache.org/licenses/LICENSE-2.0.txt");
        component.addLicense(license1);
        License license2 = new License();
        license2.setId("MIT");
        license2.setUrl("https://www.opensource.org/licenses/mit-license.php");
        component.addLicense(license2);

        SourceArtifactRefUrl comp1SourceUrl = new SourceArtifactRefUrl();
        comp1SourceUrl.setUrl("https://example.com/sources/component1-sources.jar");
        comp1SourceUrl.setMd5("11141a7cddbf60f741bc49966c38f111");
        comp1SourceUrl.setSha1("7771af8e336e372fb5399c99edabe0919aeaf777");
        component.addSources(comp1SourceUrl);

        SourceArtifactRefLocal comp1SourceArchive = new SourceArtifactRefLocal();
        comp1SourceArchive.setRelativePath(
            "file:///sources/7771af8e336e372fb5399c99edabe0919aeaf777/component1-sources.jar");
        comp1SourceArchive.setMd5("11141a7cddbf60f741bc49966c38f111");
        comp1SourceArchive.setSha1("7771af8e336e372fb5399c99edabe0919aeaf777");
        component.addSources(comp1SourceArchive);

        return component;
    }
}
