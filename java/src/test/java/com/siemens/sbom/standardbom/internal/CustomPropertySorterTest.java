/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.Arrays;
import java.util.List;

import org.cyclonedx.model.Property;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests of {@link CustomPropertySorter}.
 */
public class CustomPropertySorterTest
{
    @Test
    public void testCustomPropertySorter()
    {
        final List<Property> actual = Arrays.asList(
            buildProperty("bob", "value"),
            buildProperty("alice", "value2"),
            buildProperty("alice", "value1"),
            buildProperty("charlie", "value"),
            null,
            buildProperty("charlie", null),
            buildProperty(null, null),
            buildProperty(null, "value")
        );

        actual.sort(CustomPropertySorter.INSTANCE);

        final List<Property> expected = Arrays.asList(
            buildProperty("alice", "value1"),
            buildProperty("alice", "value2"),
            buildProperty("bob", "value"),
            buildProperty("charlie", "value"),
            buildProperty("charlie", null),
            buildProperty(null, "value"),
            buildProperty(null, null),
            null
        );

        Assert.assertEquals(expected, actual);
    }



    private Property buildProperty(final String pName, final String pValue)
    {
        Property result = new Property();
        result.setName(pName);
        result.setValue(pValue);
        return result;
    }
}
