/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;

import com.siemens.sbom.standardbom.internal.HashProcessor;


/**
 * One entry in the list of source artifacts.
 */
public abstract class AbstractSourceArtifactRef
{
    private final HashProcessor hashProc;

    private final ExternalReference cycloneDxRef;



    protected AbstractSourceArtifactRef()
    {
        this(new ExternalReference(), ExternalReference.Type.DISTRIBUTION);
    }



    protected AbstractSourceArtifactRef(@Nonnull final ExternalReference pCycloneDxRef,
        @Nonnull final ExternalReference.Type pRefType)
    {
        cycloneDxRef = Objects.requireNonNull(pCycloneDxRef, "the CycloneDX delegate was null");
        cycloneDxRef.setType(Objects.requireNonNull(pRefType, "the external reference type was null"));
        hashProc = new HashProcessor(cycloneDxRef::getHashes, cycloneDxRef::addHash);
    }



    public static boolean isSourceReference(@Nullable final ExternalReference pExternalReference)
    {
        boolean result = pExternalReference != null
            && (pExternalReference.getType() == ExternalReference.Type.SOURCE_DISTRIBUTION
            || (pExternalReference.getType() == ExternalReference.Type.DISTRIBUTION
                && SourceArtifactRefLocal.SOURCE_ARCHIVE_LOCAL.equals(pExternalReference.getComment())));
        return result;
    }



    public String getUrl()
    {
        return cycloneDxRef.getUrl();
    }



    public void setUrl(final String pUrl)
    {
        cycloneDxRef.setUrl(pUrl);
    }



    @CheckForNull
    public String getMd5()
    {
        return hashProc.get(Hash.Algorithm.MD5);
    }



    public void setMd5(@Nullable final String pMd5)
    {
        hashProc.set(Hash.Algorithm.MD5, pMd5);
    }



    @CheckForNull
    public String getSha1()
    {
        return hashProc.get(Hash.Algorithm.SHA1);
    }



    public void setSha1(@Nullable final String pSha1)
    {
        hashProc.set(Hash.Algorithm.SHA1, pSha1);
    }



    @CheckForNull
    public String getSha256()
    {
        return hashProc.get(Hash.Algorithm.SHA_256);
    }



    public void setSha256(@Nullable final String pSha256)
    {
        hashProc.set(Hash.Algorithm.SHA_256, pSha256);
    }



    @CheckForNull
    public String getSha512()
    {
        return hashProc.get(Hash.Algorithm.SHA_512);
    }



    public void setSha512(@Nullable final String pSha512)
    {
        hashProc.set(Hash.Algorithm.SHA_512, pSha512);
    }



    @Nonnull
    public ExternalReference getCycloneDxRef()
    {
        return cycloneDxRef;
    }
}
