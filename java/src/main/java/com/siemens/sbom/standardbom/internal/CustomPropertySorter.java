/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import javax.annotation.Nullable;

import org.cyclonedx.model.Property;


/**
 * Comparator for sorting CycloneDX custom component properties.
 * <p>This is a convenience feature for better supporting unit testing, because it makes properties appear in a
 * predictable order.</p>
 */
public class CustomPropertySorter
    implements Comparator<Property>, Serializable
{
    private static final long serialVersionUID = 1L;

    public static final CustomPropertySorter INSTANCE = new CustomPropertySorter();

    private static final Comparator<String> STRCOMP = Comparator.nullsLast(Comparator.comparing(String::toString));



    @Override
    public int compare(@Nullable final Property pProp1, @Nullable final Property pProp2)
    {
        int result = 0;
        if (pProp1 != null && pProp2 != null) {
            result = Objects.compare(pProp1.getName(), pProp2.getName(), STRCOMP);
            if (result == 0) {
                result = Objects.compare(pProp1.getValue(), pProp2.getValue(), STRCOMP);
            }
        }
        else if (pProp1 != null) {
            result = -1;
        }
        else if (pProp2 != null) {
            result = 1;
        }
        return result;
    }
}
