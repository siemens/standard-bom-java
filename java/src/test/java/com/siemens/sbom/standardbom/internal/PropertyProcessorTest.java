/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cyclonedx.model.Property;
import org.junit.Assert;
import org.junit.Test;

import com.siemens.sbom.standardbom.model.StandardBom;


/**
 * A few unit tests of {@link PropertyProcessor}.
 */
public class PropertyProcessorTest
{
    @Test
    public void testForeignNamespace()
    {
        Property foreign = new Property();
        foreign.setName("foreign:key");
        foreign.setValue("unit test value");

        Property other = new Property();
        other.setName("key");
        other.setValue("ERROR");

        Property regular = new Property();
        regular.setName(StandardBom.CUSTOM_PROPERTY_NAMESPACE + ":key");
        regular.setValue("regular value");

        final List<Property> props = Arrays.asList(regular, other, foreign);
        final PropertyProcessor underTest = new PropertyProcessor(() -> props, props::add);

        Assert.assertEquals("unit test value", underTest.get("foreign:key"));
        Assert.assertEquals("regular value", underTest.get("key"));  // "siemens:" is prepended if no namespace present
        Assert.assertEquals("regular value", underTest.get(StandardBom.CUSTOM_PROPERTY_NAMESPACE + ":key"));
    }



    @Test
    public void testEmptyKey()
    {
        final List<Property> props = Collections.emptyList();
        final PropertyProcessor underTest = new PropertyProcessor(() -> props, props::add);
        underTest.set("empty", "   ");  // would throw UnsupportedOperationException if adding something
    }
}
