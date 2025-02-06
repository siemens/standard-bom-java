/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.ExternalReference;


/**
 * A source artifact reference which explicitly refers to the original remote URL of the source archive.
 */
public class SourceArtifactRefUrl
    extends AbstractSourceArtifactRef
{
    public SourceArtifactRefUrl()
    {
        this(new ExternalReference());
    }



    public SourceArtifactRefUrl(@Nonnull final ExternalReference pExternalReference)
    {
        super(pExternalReference, ExternalReference.Type.SOURCE_DISTRIBUTION);
    }



    public static boolean isSourceReference(@Nullable final ExternalReference pExternalReference)
    {
        boolean result = pExternalReference != null
            && pExternalReference.getType() == ExternalReference.Type.SOURCE_DISTRIBUTION;
        return result;
    }



    @Override
    public void setUrl(final String pUrl)
    {
        super.setUrl(pUrl);
    }
}
