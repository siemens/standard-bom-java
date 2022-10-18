/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.scp.standardbom.model;

import java.util.Date;
import java.util.List;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
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
}
