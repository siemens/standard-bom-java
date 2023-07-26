/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.util.Set;
import java.util.TreeSet;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.Property;
import org.junit.Assert;
import org.junit.Test;


/**
 * Trivial unit tests for {@link BomEntry}.
 */
public class BomEntryTest
{
    /**
     * Make sure that all getters and setters are present as expected.
     */
    @Test
    public void testAccessors()
    {
        final SourceArtifactRefUrl sourceRef = new SourceArtifactRefUrl();
        sourceRef.setUrl("https://example.com");
        sourceRef.setSha1("5fa1f08842f94c146ecfd17b39cb80632ccd1c68");

        BomEntry underTest = new BomEntry();

        underTest.setType(Component.Type.LIBRARY);
        underTest.setRelativePath("value2");
        underTest.setName("value3");
        underTest.setDescription("value5");
        underTest.setFilename("value6");
        underTest.setGroup("value7");
        underTest.setPrimaryLanguage("Java");
        underTest.setPurl("pkg:maven/org.yaml/snakeyaml@1.23?type=jar");
        underTest.setBomRef("pkg:maven/org.yaml/snakeyaml@1.23?type=jar");
        underTest.setRepoUrl("value9");
        underTest.setVersion("value10");
        underTest.setAuthor("author");
        underTest.setWebsite("value11");
        underTest.addLicense(new License());
        underTest.addSources(sourceRef);
        underTest.setCopyright("statement1\nstatement2");
        underTest.setCpe("cpe:2.3:a:acme:component_framework:-:*:*:*:*:*:*:*");
        underTest.setThirdPartyNotices("notice1\nnotice2");
        underTest.setLegalRemark("Wait, that's illegal.");
        underTest.setMd5("b24f3a25bc033352f1c3ab2c683b926c");
        underTest.setSha1("725e364cc71b80e60fa450bd06d75cdea7fb2d59");
        underTest.setSha256("0c9860b8fb6f24f59e083e0b92a17c515c45312951fc272d093e4709faed6356");
        underTest.setSha512("f2c011cf1866aade9ea654d58b65e50d46ea203cb4f2230fc5c40822c158b6f144a184e00d98a8d29f"
            + "abfb4550690a6d2ff09d27960764b2810667b63600e303");

        Assert.assertEquals(Component.Type.LIBRARY, underTest.getType());
        Assert.assertEquals("value2", underTest.getRelativePath());
        Assert.assertEquals("value3", underTest.getName());
        Assert.assertEquals("value5", underTest.getDescription());
        Assert.assertEquals("value6", underTest.getFilename());
        Assert.assertEquals("value7", underTest.getGroup());
        Assert.assertEquals("pkg:maven/org.yaml/snakeyaml@1.23?type=jar", underTest.getPurl());
        Assert.assertEquals("pkg:maven/org.yaml/snakeyaml@1.23?type=jar", underTest.getBomRef());
        Assert.assertEquals("Java", underTest.getPrimaryLanguage());
        Assert.assertEquals("value9", underTest.getRepoUrl());
        Assert.assertEquals("value10", underTest.getVersion());
        Assert.assertEquals("author", underTest.getAuthor());
        Assert.assertEquals("value11", underTest.getWebsite());
        Assert.assertNotNull(underTest.getLicenses());
        Assert.assertNotNull(underTest.getSources());
        Assert.assertEquals("statement1\nstatement2", underTest.getCopyright());
        Assert.assertEquals("cpe:2.3:a:acme:component_framework:-:*:*:*:*:*:*:*", underTest.getCpe());
        Assert.assertEquals("notice1\nnotice2", underTest.getThirdPartyNotices());
        Assert.assertEquals("Wait, that's illegal.", underTest.getLegalRemark());
        Assert.assertEquals("b24f3a25bc033352f1c3ab2c683b926c", underTest.getMd5());
        Assert.assertEquals("725e364cc71b80e60fa450bd06d75cdea7fb2d59", underTest.getSha1());
        Assert.assertEquals("0c9860b8fb6f24f59e083e0b92a17c515c45312951fc272d093e4709faed6356", underTest.getSha256());
        Assert.assertEquals("f2c011cf1866aade9ea654d58b65e50d46ea203cb4f2230fc5c40822c158b6f144a184e00d98a8d29f"
            + "abfb4550690a6d2ff09d27960764b2810667b63600e303", underTest.getSha512());
    }



    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedAccessors()
    {
        final BomEntry underTest = new BomEntry();

        underTest.setNamespace("namespace");
        underTest.setArtifactId("artifactId");

        Assert.assertEquals("namespace", underTest.getNamespace());
        Assert.assertEquals("artifactId", underTest.getArtifactId());
    }



    @Test
    public void testRelativePathNull()
    {
        final BomEntry underTest = new BomEntry();
        underTest.setRelativePath(null);
        Assert.assertNull(underTest.getRelativePath());
    }



    @Test
    public void testRelativePathInvalid()
    {
        final BomEntry underTest = new BomEntry();
        underTest.setRelativePath("https://example.com/INVALID");  // We keep the invalid data, fwiw.
        Assert.assertEquals("https://example.com/INVALID", underTest.getRelativePath());
    }



    @Test
    public void testAddCopyright()
    {
        final BomEntry underTest = new BomEntry();
        underTest.addCopyright("copyright1");
        underTest.addCopyright(null);
        underTest.addCopyright("copyright2");
        Assert.assertEquals("copyright1\ncopyright2", underTest.getCopyright());
    }



    @Test
    public void testAddCopyrightWithTrim()
    {
        final BomEntry underTest = new BomEntry();
        underTest.setCopyright("  ");
        underTest.addCopyright("copyright1");
        Assert.assertEquals("copyright1", underTest.getCopyright());
    }



    @Test
    public void testAddThirdPartyNotice()
    {
        final BomEntry underTest = new BomEntry();
        underTest.addThirdPartyNotice("notice1\nline2");
        underTest.addThirdPartyNotice(null);
        underTest.addThirdPartyNotice("notice2\nline2");
        Assert.assertEquals("notice1\nline2\n\nnotice2\nline2", underTest.getThirdPartyNotices());
    }



    @Test
    public void testAddThirdPartyNoticeWithTrim()
    {
        final BomEntry underTest = new BomEntry();
        final Property spaces = new Property();
        spaces.setName(CustomProperty.THIRD_PARTY_NOTICES);
        spaces.setValue("  ");
        underTest.getCycloneDxComponent().addProperty(spaces);

        underTest.addThirdPartyNotice("notice1\nline2");
        Assert.assertEquals("notice1\nline2", underTest.getThirdPartyNotices());
    }



    @Test
    public void testRelativePathWrongType()
    {
        final BomEntry underTest = new BomEntry();
        underTest.setRelativePath("my/path");
        Assert.assertEquals("my/path", underTest.getRelativePath());
        final ExternalReference ref = underTest.getCycloneDxComponent().getExternalReferences().iterator().next();
        ref.setType(ExternalReference.Type.SOCIAL);  // break the reference by changing to a wrong type
        Assert.assertNull(underTest.getRelativePath());
    }



    @Test
    public void testRelativePathWrongComment()
    {
        final BomEntry underTest = new BomEntry();
        underTest.setRelativePath("my/path");
        Assert.assertEquals("my/path", underTest.getRelativePath());
        final ExternalReference ref = underTest.getCycloneDxComponent().getExternalReferences().iterator().next();
        ref.setComment("INVALID");  // break the reference by changing to a wrong comment
        Assert.assertNull(underTest.getRelativePath());
    }



    @Test
    public void testDirectDependencyFlag()
    {
        final BomEntry underTest = new BomEntry();
        Assert.assertNull(underTest.isDirectDependency());
        underTest.setDirectDependency(true);
        Assert.assertEquals(Boolean.TRUE, underTest.isDirectDependency());
    }



    @Test
    public void testSourceRefUrls()
    {
        final BomEntry underTest = new BomEntry();
        Assert.assertFalse(underTest.hasSourceDownloadUrls());

        final SourceArtifactRefUrl sourceRef1 = new SourceArtifactRefUrl();
        sourceRef1.setUrl("https://example.com/url1");
        sourceRef1.setSha1("5fa1f08842f94c146ecfd17b39cb80632ccd1c68");
        underTest.addSources(sourceRef1);

        final SourceArtifactRefUrl sourceRef2 = new SourceArtifactRefUrl();
        sourceRef2.setUrl("https://example.com/url2");
        sourceRef2.setSha1("4fa1f08842f94c146ecfd17b39cb80632ccd1c67");
        underTest.addSources(sourceRef2);

        final SourceArtifactRef sourceRef3 = new SourceArtifactRef();
        sourceRef3.setUrl("https://example.com/url3");
        sourceRef3.setSha1("3fa1f08842f94c146ecfd17b39cb80632ccd1c66");
        underTest.addSources(sourceRef3);

        final Set<String> expected = new TreeSet<>();
        expected.add("https://example.com/url1");
        expected.add("https://example.com/url2");
        Assert.assertTrue(underTest.hasSourceDownloadUrls());
        Assert.assertEquals(expected, underTest.getSourceDownloadUrls());
    }



    @Test
    public void testSourceRefArchives()
    {
        final BomEntry underTest = new BomEntry();
        Assert.assertFalse(underTest.hasSourceArchives());

        final SourceArtifactRefLocal sourceRef1 = new SourceArtifactRefLocal();
        sourceRef1.setRelativePath("my/archive1.jar");
        sourceRef1.setSha1("5fa1f08842f94c146ecfd17b39cb80632ccd1c68");
        underTest.addSources(sourceRef1);

        final SourceArtifactRefUrl sourceRef2 = new SourceArtifactRefUrl();
        sourceRef2.setUrl("https://example.com/url2");
        sourceRef2.setSha1("4fa1f08842f94c146ecfd17b39cb80632ccd1c67");
        underTest.addSources(sourceRef2);

        final SourceArtifactRefLocal sourceRef3 = new SourceArtifactRefLocal();
        sourceRef3.setRelativePath("my/archive3.jar");
        sourceRef3.setSha1("2fa1f08842f94c146ecfd17b39cb80632ccd1c65");
        underTest.addSources(sourceRef3);

        final SourceArtifactRef sourceRef4 = new SourceArtifactRef();
        sourceRef4.setUrl("https://example.com/url3");
        sourceRef4.setSha1("3fa1f08842f94c146ecfd17b39cb80632ccd1c66");
        underTest.addSources(sourceRef4);

        final Set<String> expected = new TreeSet<>();
        expected.add("my/archive1.jar");
        expected.add("my/archive3.jar");
        Assert.assertTrue(underTest.hasSourceArchives());
        Assert.assertEquals(expected, underTest.getSourceArchives());
    }



    @Test
    public void testHashSyntaxValidation()
    {
        final BomEntry underTest = new BomEntry();
        try {
            underTest.setSha256("INVALID HASH CODE");
            Assert.fail("expected exception was not thrown");
        }
        catch (IllegalArgumentException e) {
            Assert.assertEquals("value is not a valid SHA-256 hash: INVALID HASH CODE", e.getMessage());
        }
    }
}
