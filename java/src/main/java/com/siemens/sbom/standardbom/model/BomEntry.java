/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;

import com.siemens.sbom.standardbom.internal.ExtRefProcessor;
import com.siemens.sbom.standardbom.internal.FileProtocolHandler;
import com.siemens.sbom.standardbom.internal.HashProcessor;
import com.siemens.sbom.standardbom.internal.PropertyProcessor;


/**
 * Describes an entry in a standard-bom-compliant SBOM.
 */
@NotThreadSafe
public class BomEntry
{
    public static final String RELATIVE_PATH = "relativePath";

    private static final Predicate<ExternalReference> TYPE_RELATIVE_PATH =
        r -> ExternalReference.Type.DISTRIBUTION.equals(r.getType())
            && RELATIVE_PATH.equals(r.getComment());

    private static final Predicate<ExternalReference> TYPE_WEBSITE =
        r -> ExternalReference.Type.WEBSITE.equals(r.getType());

    private static final Predicate<ExternalReference> TYPE_VCS =
        r -> ExternalReference.Type.VCS.equals(r.getType());

    private final HashProcessor hashProc;

    private final PropertyProcessor propertyProc;

    private final ExtRefProcessor extRefProc;

    private final Component cycloneDxComponent;



    public BomEntry()
    {
        this(new Component());
    }



    public BomEntry(@Nonnull final Component pCycloneDxComponent)
    {
        cycloneDxComponent = Objects.requireNonNull(pCycloneDxComponent, "CycloneDX Component must not be null");
        hashProc = new HashProcessor(cycloneDxComponent::getHashes, cycloneDxComponent::addHash);
        propertyProc = new PropertyProcessor(cycloneDxComponent::getProperties, cycloneDxComponent::addProperty);
        extRefProc = new ExtRefProcessor(cycloneDxComponent::getExternalReferences,
            cycloneDxComponent::addExternalReference);
    }



    @CheckForNull
    public String getBomRef()
    {
        return cycloneDxComponent.getBomRef();
    }



    public void setBomRef(@Nullable final String pBomRef)
    {
        cycloneDxComponent.setBomRef(pBomRef);
    }



    public Component.Type getType()
    {
        return cycloneDxComponent.getType();
    }



    public void setType(final Component.Type pType)
    {
        cycloneDxComponent.setType(pType);
    }



    public String getGroup()
    {
        return cycloneDxComponent.getGroup();
    }



    public void setGroup(final String pGroup)
    {
        cycloneDxComponent.setGroup(pGroup);
    }



    /**
     * Getter.
     *
     * @return the namespace
     *
     * @deprecated use {@link #getGroup()} instead
     */
    @CheckForNull
    @Deprecated
    public String getNamespace()
    {
        return cycloneDxComponent.getGroup();
    }



    /**
     * Setter.
     *
     * @param pNamespace the new value
     * @deprecated use {@link #setGroup(String)} instead
     */
    @Deprecated
    public void setNamespace(final String pNamespace)
    {
        cycloneDxComponent.setGroup(pNamespace);
    }



    /**
     * Getter.
     *
     * @return the artifact ID
     *
     * @deprecated use {@link #getName()} instead
     */
    @Deprecated
    public String getArtifactId()
    {
        return getName();
    }



    /**
     * Setter.
     *
     * @param pArtifactId the new artifact ID
     * @deprecated use {@link #setName(String)} instead
     */
    @Deprecated
    public void setArtifactId(final String pArtifactId)
    {
        setName(pArtifactId);
    }



    public String getName()
    {
        return cycloneDxComponent.getName();
    }



    public void setName(final String pName)
    {
        cycloneDxComponent.setName(pName);
    }



    public String getVersion()
    {
        return cycloneDxComponent.getVersion();
    }



    public void setVersion(final String pVersion)
    {
        cycloneDxComponent.setVersion(pVersion);
    }



    public String getPurl()
    {
        return cycloneDxComponent.getPurl();
    }



    public void setPurl(final String pPurl)
    {
        cycloneDxComponent.setPurl(pPurl);
    }



    @CheckForNull
    public String getAuthor()
    {
        return cycloneDxComponent.getAuthor();
    }



    public void setAuthor(@Nullable final String pAuthor)
    {
        cycloneDxComponent.setAuthor(pAuthor);
    }



    public String getDescription()
    {
        return cycloneDxComponent.getDescription();
    }



    public void setDescription(final String pDescription)
    {
        cycloneDxComponent.setDescription(pDescription);
    }



    @CheckForNull
    public String getPrimaryLanguage()
    {
        return propertyProc.get(CustomProperty.PRIMARY_LANGUAGE);
    }



    public void setPrimaryLanguage(@Nullable final String pPrimaryLanguage)
    {
        propertyProc.set(CustomProperty.PRIMARY_LANGUAGE, pPrimaryLanguage);
    }



    @CheckForNull
    public Boolean isDirectDependency()
    {
        String v = propertyProc.get(CustomProperty.DIRECT_DEPENDENCY);
        if (v != null) {
            return Boolean.parseBoolean(v);
        }
        return null;
    }



    public void setDirectDependency(final boolean pDirectDependency)
    {
        propertyProc.set(CustomProperty.DIRECT_DEPENDENCY, Boolean.toString(pDirectDependency));
    }



    @CheckForNull
    public String getLegalRemark()
    {
        return propertyProc.get(CustomProperty.LEGAL_REMARK);
    }



    public void setLegalRemark(@Nullable final String pLegalRemark)
    {
        propertyProc.set(CustomProperty.LEGAL_REMARK, pLegalRemark);
    }



    @CheckForNull
    public String getFilename()
    {
        return propertyProc.get(CustomProperty.FILENAME);
    }



    public void setFilename(@Nullable final String pFilename)
    {
        propertyProc.set(CustomProperty.FILENAME, pFilename);
    }



    @CheckForNull
    public String getWebsite()
    {
        return extRefProc.get(TYPE_WEBSITE);
    }



    public void setWebsite(@Nullable final String pWebsite)
    {
        extRefProc.set(TYPE_WEBSITE, pWebsite, ref -> ref.setType(ExternalReference.Type.WEBSITE));
    }



    @CheckForNull
    public String getRepoUrl()
    {
        return extRefProc.get(TYPE_VCS);
    }



    public void setRepoUrl(@Nullable final String pRepoUrl)
    {
        extRefProc.set(TYPE_VCS, pRepoUrl, ref -> ref.setType(ExternalReference.Type.VCS));
    }



    @CheckForNull
    public String getRelativePath()
    {
        return FileProtocolHandler.withoutFileProtocol(extRefProc.get(TYPE_RELATIVE_PATH));
    }



    public void setRelativePath(@Nullable final String pRelativePath)
    {
        String relativePath = pRelativePath;
        if (relativePath != null && !relativePath.contains(":")) {
            relativePath = relativePath.replaceAll(Pattern.quote("\\"), "/");
            relativePath = FileProtocolHandler.ensureFileProtocol(relativePath);
        }
        extRefProc.set(TYPE_RELATIVE_PATH, relativePath, ref -> {
            ref.setType(ExternalReference.Type.DISTRIBUTION);
            ref.setComment(RELATIVE_PATH);
        });
    }



    @Nonnull
    public LicenseChoice getLicenses()
    {
        if (cycloneDxComponent.getLicenseChoice() == null) {
            cycloneDxComponent.setLicenseChoice(new LicenseChoice());
        }
        return cycloneDxComponent.getLicenseChoice();
    }



    public void addLicense(final License pLicense)
    {
        getLicenses().addLicense(pLicense);
    }



    @CheckForNull
    public String getCopyright()
    {
        return cycloneDxComponent.getCopyright();
    }



    public void setCopyright(@Nullable final String pCopyright)
    {
        cycloneDxComponent.setCopyright(pCopyright);
    }



    /**
     * Add a new copyright statement on a new line.
     *
     * @param pCopyright the statement to add
     */
    public void addCopyright(@Nullable final String pCopyright)
    {
        if (pCopyright != null) {
            String previousStatements = cycloneDxComponent.getCopyright();
            if (previousStatements != null && !previousStatements.trim().isEmpty()) {
                previousStatements += "\n";
            }
            else {
                previousStatements = "";
            }
            cycloneDxComponent.setCopyright(previousStatements + pCopyright.trim());
        }
    }



    @CheckForNull
    public String getCpe()
    {
        return cycloneDxComponent.getCpe();
    }



    public void setCpe(@Nullable final String pCpeName)
    {
        cycloneDxComponent.setCpe(pCpeName);
    }



    @CheckForNull
    public String getThirdPartyNotices()
    {
        return propertyProc.get(CustomProperty.THIRD_PARTY_NOTICES);
    }



    public void setThirdPartyNotices(@Nullable final String pThirdPartyNotices)
    {
        propertyProc.set(CustomProperty.THIRD_PARTY_NOTICES, pThirdPartyNotices);
    }



    /**
     * Add a new third party notice separated by a newline.
     *
     * @param pThirdPartyNotice the notice to add
     */
    public void addThirdPartyNotice(@Nullable final String pThirdPartyNotice)
    {
        if (pThirdPartyNotice != null) {
            String previousNotices = getThirdPartyNotices();
            if (previousNotices != null && !previousNotices.trim().isEmpty()) {
                previousNotices += "\n\n";
            }
            else {
                previousNotices = "";
            }
            setThirdPartyNotices(previousNotices + pThirdPartyNotice.trim());
        }
    }



    @Nonnull
    public List<SourceArtifactRef> getSources()
    {
        final List<SourceArtifactRef> sources = new ArrayList<>();
        if (cycloneDxComponent.getExternalReferences() != null) {
            for (ExternalReference ref : cycloneDxComponent.getExternalReferences()) {
                if (SourceArtifactRefLocal.isSourceReference(ref)) {
                    sources.add(new SourceArtifactRefLocal(ref));
                }
                else if (SourceArtifactRefUrl.isSourceReference(ref)) {
                    sources.add(new SourceArtifactRefUrl(ref));
                }
                else if (SourceArtifactRef.isSourceReference(ref)) {
                    sources.add(new SourceArtifactRef(ref));
                }
            }
        }
        return Collections.unmodifiableList(sources);
    }



    public void addSources(@Nonnull final SourceArtifactRef pSourceArtifactRef)
    {
        cycloneDxComponent.addExternalReference(pSourceArtifactRef.getCycloneDxRef());
    }



    public boolean hasSourceArchives()
    {
        return !findSourceArchives().isEmpty();
    }



    @Nonnull
    public Set<String> getSourceArchives()
    {
        return findSourceArchives();
    }



    @Nonnull
    private Set<String> findSourceArchives()
    {
        Set<String> result = getSources().stream()
            .filter(SourceArtifactRefLocal.class::isInstance)
            .map(SourceArtifactRefLocal.class::cast)
            .map(SourceArtifactRefLocal::getRelativePath)
            .collect(Collectors.toSet());
        return Collections.unmodifiableSet(result);
    }



    public boolean hasSourceDownloadUrls()
    {
        return !findSourceDownloadUrls().isEmpty();
    }



    @Nonnull
    public Set<String> getSourceDownloadUrls()
    {
        return findSourceDownloadUrls();
    }



    @Nonnull
    private Set<String> findSourceDownloadUrls()
    {
        Set<String> result = getSources().stream()
            .filter(SourceArtifactRefUrl.class::isInstance)
            .map(SourceArtifactRefUrl.class::cast)
            .map(SourceArtifactRefUrl::getUrl)
            .collect(Collectors.toSet());
        return Collections.unmodifiableSet(result);
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
    public Component getCycloneDxComponent()
    {
        return cycloneDxComponent;
    }
}
