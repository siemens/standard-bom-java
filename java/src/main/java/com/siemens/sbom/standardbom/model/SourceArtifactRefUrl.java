/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.ExternalReference;


/**
 * A source artifact reference which explicitly refers to the original remote URL of the source archive.
 */
public class SourceArtifactRefUrl
    extends SourceArtifactRef
{
    public static final String SOURCE_ARCHIVE_URL = SOURCE_ARCHIVE + " (download location)";



    public SourceArtifactRefUrl()
    {
        this(new ExternalReference());
    }



    public SourceArtifactRefUrl(@Nonnull final ExternalReference pExternalReference)
    {
        super(pExternalReference);
        pExternalReference.setComment(SOURCE_ARCHIVE_URL);
    }



    public static boolean isSourceReference(@Nullable final ExternalReference pExternalReference)
    {
        boolean result = pExternalReference != null
            && SourceArtifactRef.isSourceReference(pExternalReference)
            && SOURCE_ARCHIVE_URL.equals(pExternalReference.getComment());
        return result;
    }



    @Override
    public void setUrl(final String pUrl)
    {
        super.setUrl(pUrl);
        getCycloneDxRef().setComment(SOURCE_ARCHIVE_URL);
    }
}
