/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom.model;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;


/**
 * Possible values of the <code>siemens:sbomNature</code> property.
 */
public enum SbomNature
{
    /** The SBOM contains binary components. */
    Binary,

    /** The SBOM contains source components. */
    Source;



    /**
     * Converts a string to one of the enum values, if possible. If there is no match, <code>null</code> is returned.
     *
     * @param pValue any string, ideally one of the enum values (case-insensitive)
     * @return the matching enum constant, or <code>null</code> if there was no match
     */
    @CheckForNull
    public static SbomNature parseNature(@Nullable final String pValue)
    {
        SbomNature result = null;
        if (pValue != null) {
            for (SbomNature ev : SbomNature.values()) {
                if (ev.name().equalsIgnoreCase(pValue)) {
                    result = ev;
                    break;
                }
            }
        }
        return result;
    }
}
