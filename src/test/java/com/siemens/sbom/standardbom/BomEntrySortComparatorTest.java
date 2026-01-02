/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cyclonedx.model.Component;
import org.junit.Assert;
import org.junit.Test;

import com.siemens.sbom.standardbom.model.BomEntry;


/**
 * Unit tests of the {@link BomEntrySortComparator}.
 */
public class BomEntrySortComparatorTest
{
    @Test
    public void testSort1()
    {
        // --- ARRANGE ---
        final BomEntry entry1 = buildEntry(Component.Type.LIBRARY, "group1", "artifact1", "1.0",
            "filename.jar", "3f18e1aa31031d89db6f01ba05d501258ce69d2c");
        final BomEntry entry2 = buildEntry(Component.Type.LIBRARY, "group2", "artifact2", "2.0",
            "filename2.jar", "226247b40160f2892fa4c7851b5b913d5d10912d");
        final BomEntry entry3 = buildEntry(Component.Type.LIBRARY, null, null, null,
            "filename3.jar", "226247b40160f2892fa4c7851b5b913d5d10912d");
        final BomEntry entry4 = buildEntry(Component.Type.LIBRARY, "group1", "artifact1a", "1.0",
            "filename.jar", "f5e9a2ffca496057d6891a3de65128efc636e26e");
        final BomEntry entry5 = buildEntry(Component.Type.LIBRARY, "group1", "artifact1", "1.1",
            "filename.jar", "a11bdc0e8f95da31527b4f34f1a35c23e197498d");
        final BomEntry entry6 = buildEntry(Component.Type.LIBRARY, null, null, null,
            "filename3.jar", "a11bdc0e8f95da31527b4f34f1a35c23e197498d");
        final BomEntry entry7 = buildEntry(entry2.getType(), entry2.getGroup(), entry2.getName(),
            entry2.getVersion(), entry2.getFilename(), entry2.getSha1());

        final BomEntry entry10 = buildEntry(Component.Type.APPLICATION, null, "artifact3", "1.0", null, null);
        final BomEntry entry11 = buildEntry(Component.Type.APPLICATION, null, "artifact3", "2.0", null, null);

        final BomEntry entry8 = buildEntry(Component.Type.FILE, null, null, null,
            "alice.jar", "6c2fb3f5b7cd27504726aef1b674b542a0c9cf53");
        final BomEntry entry9 = buildEntry(Component.Type.FILE, null, null, null,
            "bob.jar", "338d2678d341eedbc5273f983766dc65ffed13c2");

        final List<BomEntry> actual = new ArrayList<>();
        actual.add(entry8);
        actual.add(entry11);
        actual.add(entry9);
        actual.add(entry3);
        actual.add(entry6);
        actual.add(entry1);
        actual.add(null);
        actual.add(null);
        actual.add(entry2);
        actual.add(entry10);
        actual.add(entry4);
        actual.add(entry5);
        actual.add(entry2);  // a second time, to be filtered out
        actual.add(entry7);  // identical to entry2, but not the same object

        // --- ACT ---
        actual.sort(new BomEntrySortComparator());

        // --- ASSERT ---
        final boolean twoFirst = System.identityHashCode(entry2) < System.identityHashCode(entry7);
        final List<BomEntry> expected = Arrays.asList(
            entry10,
            entry11,
            entry1,
            entry5,
            entry4,
            twoFirst ? entry2 : entry7,
            entry2,
            twoFirst ? entry7 : entry2,
            entry3,
            entry6,
            entry8,
            entry9,
            null,
            null);

        Assert.assertEquals(expected, actual);
    }



    private BomEntry buildEntry(final Component.Type pType, final String pGroup, final String pName,
        final String pVersion, final String pFilename, final String pSha1)
    {
        BomEntry result = new BomEntry();
        result.setType(pType);

        result.setGroup(pGroup);
        result.setName(pName);
        result.setVersion(pVersion);

        result.setFilename(pFilename);
        result.setSha1(pSha1);

        return result;
    }
}
