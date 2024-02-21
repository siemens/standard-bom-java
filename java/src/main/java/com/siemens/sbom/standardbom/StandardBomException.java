/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
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
