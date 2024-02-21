/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Component;


/**
 * Static helper methods.
 */
public final class StandardBomUtil
{
    private StandardBomUtil()
    {
        // utility class
    }



    /**
     * Convert the string representation of a {@link Component.Type} into a real {@link Component.Type}.
     *
     * @param pString the type name in string format as per {@link Component.Type#getTypeName()}
     * @return the component type, or <code>null</code> if no matching type could be determined
     */
    @CheckForNull
    public static Component.Type string2compType(@Nullable final String pString)
    {
        Component.Type result = null;
        for (Component.Type t : Component.Type.values()) {
            if (t.getTypeName().equals(pString)) {
                result = t;
                break;
            }
        }
        return result;
    }
}
