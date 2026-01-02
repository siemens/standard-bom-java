/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom;

/**
 * An error occurred processing a Standard BOM.
 */
public class StandardBomException
    extends RuntimeException
{
    public StandardBomException(final String pMessage)
    {
        super(pMessage);
    }



    public StandardBomException(final String pMessage, final Throwable pCause)
    {
        super(pMessage, pCause);
    }
}
