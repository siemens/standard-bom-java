/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;

import com.siemens.sbom.standardbom.StandardBomUtil;
import com.siemens.sbom.standardbom.internal.StringMapProcessor;


/**
 * A component which is referenced by this SBOM, but which is not itself part of it. For example, we might reference
 * a component in a backend system. In terms of CycloneDX, this is not a {@link org.cyclonedx.model.Component
 * Component}, but an {@link ExternalReference}.
 */
public class ExternalComponent
{
    /** In BlackDuck CodeCenter, IDs are decimal numbers of up to 12 digits. */
    public static final Pattern BLACKDUCK_COMPONENT_ID_PATTERN = Pattern.compile("\\d{1,12}");

    /** In SW360, IDs are 32-digit hexadecimal values. */
    public static final Pattern SW360_ID_PATTERN = Pattern.compile("[0-9a-fA-F]{32}");

    private static final Pattern PURL_PATTERN = Pattern.compile("pkg:[a-z]+/.+");

    /** A PackageURL that SCP understands as "I don't know the purl". Still a valid purl which prevents the CycloneDX
     *  serializer from skipping this object. We use this as a default value for the {@code url} field, until the
     *  PackageURL specification introduces an official value for this case. */
    public static final String UNKNOWN_PURL = "pkg:generic/com.siemens.scp/no-purl";

    private final ExternalReference cycloneDxRef;

    private final StringMapProcessor stringMapProc;



    public ExternalComponent()
    {
        this(new ExternalReference());
    }



    public ExternalComponent(@Nonnull final ExternalReference pCycloneDxRef)
    {
        cycloneDxRef = Objects.requireNonNull(pCycloneDxRef, "CycloneDX delegate was null");
        cycloneDxRef.setType(ExternalReference.Type.OTHER);
        cycloneDxRef.setUrl(UNKNOWN_PURL);
        stringMapProc = new StringMapProcessor(cycloneDxRef::getComment, cycloneDxRef::setComment);
    }



    public static boolean isExternalComponent(@Nullable final ExternalReference pExtRef)
    {
        boolean result = pExtRef != null
            && pExtRef.getType() == ExternalReference.Type.OTHER
            && pExtRef.getUrl() != null
            && PURL_PATTERN.matcher(pExtRef.getUrl()).matches()
            && canBeDeserializedAsProperties(pExtRef.getComment());
        return result;
    }



    private static boolean canBeDeserializedAsProperties(@Nullable final String pSerialized)
    {
        boolean result = true;
        if (pSerialized != null) {
            result = false;
            try (StringReader sr = new StringReader(pSerialized)) {
                new Properties().load(sr);
                result = true;
            }
            catch (IOException | RuntimeException e) {
                // stay at false
            }
        }
        return result;
    }



    @CheckForNull
    public String getExternalId()
    {
        return stringMapProc.get("ExternalId");
    }



    /**
     * Set the external ID which identifies this component in the external system.
     *
     * @param pExternalId the external ID
     * @param pValidationPattern optional validation pattern for the ID, for example {@link #SW360_ID_PATTERN} or
     * {@link #BLACKDUCK_COMPONENT_ID_PATTERN}
     */
    public void setExternalId(@Nullable final String pExternalId, @Nullable final Pattern pValidationPattern)
    {
        if (pExternalId != null && pValidationPattern != null && !pValidationPattern.matcher(pExternalId).matches()) {
            throw new IllegalArgumentException("ExternalId \"" + pExternalId
                + "\" does not match the validation pattern: " + pValidationPattern.pattern());
        }
        stringMapProc.set("ExternalId", pExternalId);
    }



    /**
     * Set the external ID which identifies this component in the external system. Validation is not performed.
     *
     * @param pExternalId the external ID
     * @see #setExternalId(String, Pattern)
     */
    public void setExternalId(@Nullable final String pExternalId)
    {
        setExternalId(pExternalId, null);
    }



    /**
     * Getter.
     *
     * @return the purl of the referenced component
     *
     * @deprecated use {@link #getUrl()} instead
     */
    @Deprecated
    public String getPurl()
    {
        return getUrl();
    }



    /**
     * Setter.
     *
     * @param pPurl the purl of the referenced component
     * @deprecated use {@link #setUrl} instead
     */
    @Deprecated
    public void setPurl(final String pPurl)
    {
        setUrl(pPurl);
    }



    public String getUrl()
    {
        return cycloneDxRef.getUrl();
    }



    public void setUrl(@Nullable final String pPurl)
    {
        if (pPurl == null || PURL_PATTERN.matcher(pPurl).matches()) {
            cycloneDxRef.setUrl(pPurl);
        }
        else {
            throw new IllegalArgumentException("value is not a purl: " + pPurl);
        }
    }



    @CheckForNull
    public String getLegalRemark()
    {
        return stringMapProc.get("LegalRemark");
    }



    public void setLegalRemark(@Nullable final String pLegalRemark)
    {
        stringMapProc.set("LegalRemark", pLegalRemark);
    }



    @CheckForNull
    public Component.Type getType()
    {
        String s = stringMapProc.get("Type");
        return StandardBomUtil.string2compType(s);
    }



    public void setType(@Nullable final Component.Type pType)
    {
        stringMapProc.set("Type", pType != null ? pType.getTypeName() : null);
    }



    @CheckForNull
    public String getDescription()
    {
        return stringMapProc.get("Description");
    }



    public void setDescription(@Nullable final String pDescription)
    {
        stringMapProc.set("Description", pDescription);
    }



    @Nonnull
    public ExternalReference getCycloneDxRef()
    {
        return cycloneDxRef;
    }
}
