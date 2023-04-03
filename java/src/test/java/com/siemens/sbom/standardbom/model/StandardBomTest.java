/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Property;
import org.junit.Assert;
import org.junit.Test;


/**
 * Some unit tests of {@link StandardBom}.
 */
public class StandardBomTest
{
    @Test
    public void testTimestamp()
    {
        final StandardBom underTest = new StandardBom();
        final Date timestamp = new Date();
        underTest.setTimestamp(timestamp);
        Assert.assertEquals(timestamp, underTest.getTimestamp());
    }



    @Test
    public void testEmptyDependencies()
    {
        final StandardBom underTest = new StandardBom();
        List<Dependency> actualDeps = underTest.getDependencies();
        Assert.assertNotNull(actualDeps);
        Assert.assertTrue(actualDeps.isEmpty());
    }



    @Test
    public void testDependencies()
    {
        final StandardBom underTest = new StandardBom();

        final Dependency dep1 = new Dependency("dep1");
        underTest.addDependency(dep1);
        List<Dependency> actualDeps = underTest.getDependencies();
        Assert.assertNotNull(actualDeps);
        Assert.assertEquals(1, actualDeps.size());

        final Dependency dep2 = new Dependency("dep2");
        underTest.addDependency(dep2);
        actualDeps = underTest.getDependencies();
        Assert.assertNotNull(actualDeps);
        Assert.assertEquals(2, actualDeps.size());
    }



    @Test
    public void testAddComponent()
    {
        final StandardBom underTest = new StandardBom();

        Assert.assertNotNull(underTest.getComponents());
        Assert.assertTrue(underTest.getComponents().isEmpty());

        underTest.addComponent(new BomEntry());
        Assert.assertNotNull(underTest.getComponents());
        Assert.assertFalse(underTest.getComponents().isEmpty());
    }



    @Test
    public void testAddExternalComponent()
    {
        final ExternalComponent extComp = new ExternalComponent();

        extComp.setExternalId("41983931");
        extComp.setType(Component.Type.APPLICATION);
        extComp.setDescription("description text");
        extComp.setLegalRemark("legal remark");
        extComp.setUrl("pkg:maven/org.yaml/snakeyaml@1.23");

        final StandardBom underTest = new StandardBom();
        Assert.assertNull(underTest.getCycloneDxBom().getExternalReferences());

        underTest.addExternalComponent(extComp);
        Assert.assertNotNull(underTest.getCycloneDxBom().getExternalReferences());
        Assert.assertFalse(underTest.getCycloneDxBom().getExternalReferences().isEmpty());
        final ExternalReference cycloneDxRef = underTest.getCycloneDxBom().getExternalReferences().get(0);
        Assert.assertEquals("pkg:maven/org.yaml/snakeyaml@1.23", cycloneDxRef.getUrl());
        Assert.assertEquals("Description=description text\nExternalId=41983931\n"
            + "LegalRemark=legal remark\nType=application", cycloneDxRef.getComment());
    }



    @Test
    public void testGetExternalComponents()
    {
        final ExternalComponent extComp1 = new ExternalComponent();
        extComp1.setExternalId("41983931");
        extComp1.setType(Component.Type.APPLICATION);
        extComp1.setDescription("description text");
        extComp1.setLegalRemark("legal remark");
        extComp1.setUrl("pkg:maven/org.yaml/snakeyaml@1.23");

        final ExternalComponent extComp2 = new ExternalComponent();
        extComp2.setUrl("pkg:maven/org.yaml/snailyaml@1.1");

        final ExternalReference otherRef = new ExternalReference();
        otherRef.setType(ExternalReference.Type.SOCIAL);
        otherRef.setUrl("https://example.com");

        final StandardBom underTest = new StandardBom();
        underTest.addExternalComponent(extComp1);
        underTest.getCycloneDxBom().addExternalReference(otherRef);
        underTest.addExternalComponent(extComp2);

        final List<ExternalComponent> actual = underTest.getExternalComponents();
        Assert.assertEquals(2, actual.size());
    }



    @Test
    public void testGetExternalComponentsNull()
    {
        final StandardBom underTest = new StandardBom();
        final List<ExternalComponent> actual = underTest.getExternalComponents();
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.isEmpty());
    }



    @Test
    public void testProfileAccessor()
    {
        final String testProfileName = "unittest";

        final StandardBom underTest = new StandardBom();
        underTest.setProfile(testProfileName);

        Assert.assertEquals(testProfileName, underTest.getProfile());
        Assert.assertNotNull(underTest.getCycloneDxBom());
        Assert.assertNotNull(underTest.getCycloneDxBom().getMetadata());
        final List<Property> actualProps = underTest.getCycloneDxBom().getMetadata().getProperties();
        Assert.assertNotNull(actualProps);
        Assert.assertEquals(1, actualProps.size());
        Assert.assertNotNull(actualProps.get(0));
        Assert.assertEquals(StandardBom.CUSTOM_PROPERTY_NAMESPACE + ":" + CustomProperty.PROFILE,
            actualProps.get(0).getName());
        Assert.assertEquals(testProfileName, actualProps.get(0).getValue());
    }



    @Test
    public void testTimestampParsing()
        throws IOException
    {
        ValidTimestamps actual = null;
        try (InputStream is = getClass().getResourceAsStream("valid-timestamps.json")) {
            actual = new ObjectMapper().readValue(is, ValidTimestamps.class);
        }

        Assert.assertNotNull(actual);
        Assert.assertEquals(new Date(1674654121000L), actual.date1);
        Assert.assertEquals(new Date(1674657721000L), actual.date2);
        Assert.assertEquals(new Date(1674657721000L), actual.date3);
        Assert.assertEquals(new Date(1674657721000L), actual.date4);
        Assert.assertEquals(new Date(1674654121000L), actual.date5);
        Assert.assertEquals(new Date(1674641521000L), actual.date6);
        Assert.assertEquals(new Date(1674657720000L), actual.date7);
        Assert.assertEquals(new Date(1674654120000L), actual.date8);
    }



    @Test
    public void testSerialNumber()
    {
        final StandardBom underTest = new StandardBom();
        final String sn = "urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79";
        underTest.setSerialNumber(sn);
        Assert.assertEquals(sn, underTest.getSerialNumber());
    }
}
