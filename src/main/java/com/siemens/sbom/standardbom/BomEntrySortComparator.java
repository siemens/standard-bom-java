/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import com.siemens.sbom.standardbom.model.BomEntry;


/**
 * Define an order on BOM entries, for sorting a given set of BOM entries. This order is <i>not</i> valid for finding
 * duplicates, only for sorting.
 * <p>If any objects or values are <code>null</code>, they are put at the end (<i>nulls last</i>).</p>
 * Elements are sorted by:
 * <ol>
 *     <li><a href="https://cyclonedx.org/docs/1.4/json/#components_items_type">type</a></li>
 *     <li><a href="https://cyclonedx.org/docs/1.4/json/#components_items_group">group</a></li>
 *     <li><a href="https://cyclonedx.org/docs/1.4/json/#components_items_name">name</a></li>
 *     <li>filename</li>
 *     <li><a href="https://cyclonedx.org/docs/1.4/json/#components_items_version">version</a></li>
 *     <li>SHA1</li>
 * </ol>
 * <p>If elements are still equal after that, they are finally sorted by the system hash code in the current JVM. This
 * is mostly useful for unit testing.</p>
 */
public class BomEntrySortComparator
    implements Comparator<BomEntry>, Serializable
{
    private static final long serialVersionUID = 2L;



    @Override
    public int compare(final BomEntry pDto1, final BomEntry pDto2)
    {
        int result = compareByNulls(pDto1, pDto2);

        if (result == 0 && pDto1 != null) {
            result = compareByType(pDto1, pDto2);
        }

        if (result == 0 && pDto1 != null) {
            final Comparator<String> strcomp = Comparator.nullsLast(Comparator.comparing(String::toString));

            result = Objects.compare(pDto1.getGroup(), pDto2.getGroup(), strcomp);
            if (result == 0) {
                result = Objects.compare(pDto1.getName(), pDto2.getName(), strcomp);
            }
            if (result == 0) {
                result = Objects.compare(pDto1.getFilename(), pDto2.getFilename(), strcomp);
            }
            if (result == 0) {
                result = Objects.compare(pDto1.getVersion(), pDto2.getVersion(), strcomp);
            }
            if (result == 0) {
                result = Objects.compare(pDto1.getSha1(), pDto2.getSha1(), strcomp);
            }
        }

        if (result == 0 && pDto1 != null) {
            result = compareByIdentity(pDto1, pDto2);
        }
        return result;
    }



    private int compareByNulls(final Object pDto1, final Object pDto2)
    {
        int result = 0;
        if (pDto1 == null && pDto2 != null) {
            result = 1;
        }
        else if (pDto1 != null && pDto2 == null) {
            result = -1;
        }
        return result;
    }



    private int compareByType(final BomEntry pDto1, final BomEntry pDto2)
    {
        int typeOrder1 = pDto1.getType() != null ? pDto1.getType().ordinal() : Integer.MAX_VALUE;
        int typeOrder2 = pDto2.getType() != null ? pDto2.getType().ordinal() : Integer.MAX_VALUE;
        return Integer.compare(typeOrder1, typeOrder2);
    }



    private int compareByIdentity(final Object pDto1, final Object pDto2)
    {
        int result = 0;
        int hash1 = System.identityHashCode(pDto1);
        int hash2 = System.identityHashCode(pDto2);
        if (hash1 < hash2) {
            result = -1;
        }
        else if (hash1 > hash2) {
            result = 1;
        }
        return result;
    }
}
