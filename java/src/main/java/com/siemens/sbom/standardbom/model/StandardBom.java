/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.definition.Definition;
import org.cyclonedx.model.definition.Standard;
import org.cyclonedx.model.metadata.ToolInformation;

import com.siemens.sbom.standardbom.internal.PropertyProcessor;
import com.siemens.sbom.standardbom.internal.VersionUtil;


/**
 * Main DTO for the complete "Standard BOM" JSON structure.
 * <p><a href="https://sbom.siemens.io/v2/format.html">Format Description</a></p>
 */
public class StandardBom
{
    /**
     * Namespace of our component custom properties as per <!-- @formatter:off --><a
     * href="https://github.com/CycloneDX/cyclonedx-property-taxonomy">https://github.com/CycloneDX/cyclonedx-property-taxonomy</a>
     * <!-- @formatter:on -->
     */
    public static final String CUSTOM_PROPERTY_NAMESPACE = "siemens";

    public static final String SPEC_NAME = "Standard BOM";

    private static final String SPEC_OWNER = "Siemens AG";

    private final PropertyProcessor metaPropProc;

    private final Bom cycloneDxSbom;



    public StandardBom()
    {
        this(new Bom());
        getCycloneDxBom().setDefinitions(getStandardBomSpecDescriptor());
        getMetadata().setToolChoice(getStandardBomJavaLibDescriptor());
    }



    public StandardBom(@Nonnull final Bom pCycloneDxSbom)
    {
        cycloneDxSbom = Objects.requireNonNull(pCycloneDxSbom, "SBOM must not be null");
        metaPropProc = new PropertyProcessor(() -> getMetadata().getProperties(), p -> getMetadata().addProperty(p));
    }



    private Definition getStandardBomSpecDescriptor()
    {
        Standard standard = new Standard();
        standard.setBomRef(VersionUtil.getSpecToolName());
        standard.setName(SPEC_NAME);
        standard.setVersion(VersionUtil.getSpecVersion());
        standard.setDescription("Siemens SBOM Standard");
        standard.setOwner(SPEC_OWNER);

        final ExternalReference website = new ExternalReference();
        website.setType(ExternalReference.Type.WEBSITE);
        website.setUrl(VersionUtil.getSpecWebsite());
        standard.setExternalReferences(Collections.singletonList(website));

        Definition result = new Definition();
        List<Standard> standards = new ArrayList<>();  // stay mutable
        standards.add(standard);
        result.setStandards(standards);
        return result;
    }



    private ToolInformation getStandardBomJavaLibDescriptor()
    {
        final Component javaLib = new Component();
        javaLib.setGroup(VersionUtil.getLibraryGroup());
        javaLib.setName(VersionUtil.getLibraryName());
        javaLib.setVersion(VersionUtil.getLibraryVersion());
        javaLib.setDescription(VersionUtil.getLibraryDescription());
        OrganizationalEntity supplier = new OrganizationalEntity();
        supplier.setName("Siemens AG");
        javaLib.setSupplier(supplier);

        final ExternalReference website = new ExternalReference();
        website.setType(ExternalReference.Type.WEBSITE);
        website.setUrl(VersionUtil.getLibraryWebsite());
        javaLib.setExternalReferences(Collections.singletonList(website));

        final ToolInformation result = new ToolInformation();
        List<Component> components = new ArrayList<>();  // stay mutable
        components.add(javaLib);
        result.setComponents(components);
        return result;
    }



    @Nonnull
    public Date getTimestamp()
    {
        Date timestamp = getMetadata().getTimestamp();
        if (timestamp == null) {
            timestamp = new Date(0);
        }
        return timestamp;
    }



    public void setTimestamp(@Nonnull final Date pTimestamp)
    {
        getMetadata().setTimestamp(Objects.requireNonNull(pTimestamp, "timestamp cannot be null"));
    }



    public Metadata getMetadata()
    {
        Metadata result = cycloneDxSbom.getMetadata();
        if (result == null) {
            result = new Metadata();
            cycloneDxSbom.setMetadata(result);
        }
        return result;
    }



    @Nonnull
    public List<BomEntry> getComponents()
    {
        if (cycloneDxSbom.getComponents() == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(
            cycloneDxSbom.getComponents().stream().map(BomEntry::new).collect(Collectors.toList()));
    }



    public void addComponent(@Nonnull final BomEntry pBomEntry)
    {
        cycloneDxSbom.addComponent(pBomEntry.getCycloneDxComponent());
    }



    @Nonnull
    public List<Dependency> getDependencies()
    {
        List<Dependency> result = cycloneDxSbom.getDependencies();
        if (result == null) {
            result = new ArrayList<>();
            cycloneDxSbom.setDependencies(result);
        }
        return result;
    }



    public void addDependency(@Nonnull final Dependency pDependency)
    {
        cycloneDxSbom.addDependency(Objects.requireNonNull(pDependency));
    }



    @Nonnull
    public List<ExternalComponent> getExternalComponents()
    {
        final List<ExternalComponent> result = new ArrayList<>();
        if (cycloneDxSbom.getExternalReferences() != null) {
            for (ExternalReference extRef : cycloneDxSbom.getExternalReferences()) {
                if (ExternalComponent.isExternalComponent(extRef)) {
                    result.add(new ExternalComponent(extRef));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }



    public void addExternalComponent(@Nonnull final ExternalComponent pExternalComponent)
    {
        cycloneDxSbom.addExternalReference(pExternalComponent.getCycloneDxRef());
    }



    public void setProfile(@Nullable final String pProfile)
    {
        metaPropProc.set(CustomProperty.PROFILE, pProfile);
    }



    @CheckForNull
    public String getProfile()
    {
        return metaPropProc.get(CustomProperty.PROFILE);
    }



    public void setSbomNature(@Nullable final SbomNature pNature)
    {
        metaPropProc.set(CustomProperty.SBOM_NATURE,
            pNature != null ? pNature.name().toLowerCase(Locale.ENGLISH) : null);
    }



    @CheckForNull
    public SbomNature getSbomNature()
    {
        return SbomNature.parseNature(metaPropProc.get(CustomProperty.SBOM_NATURE));
    }



    public void setSerialNumber(@Nullable final String pSerialNumber)
    {
        cycloneDxSbom.setSerialNumber(pSerialNumber);
    }



    @CheckForNull
    public String getSerialNumber()
    {
        return cycloneDxSbom.getSerialNumber();
    }



    /**
     * Determine if this SBOM contains a <a href="https://sbom.siemens.io/v3/format.html#definitionsstandards">Standard
     * BOM declaration</a>, and if so, return its version number.
     *
     * @return the Standard BOM version number declared in this SBOM represented by this object, or <code>null</code>
     * if no such information can be found
     */
    @CheckForNull
    public String getStandardBomVersion()
    {
        String result = getStandardBomVersionFromStandards();
        if (result == null) {
            result = getStandardBomVersionFromTools();
        }
        return result;
    }



    @CheckForNull
    private String getStandardBomVersionFromStandards()
    {
        String result = null;
        Definition definitions = getCycloneDxBom().getDefinitions();
        if (definitions != null) {
            List<Standard> standards = definitions.getStandards();
            if (standards != null) {
                for (Standard standard : standards) {
                    if (SPEC_NAME.equals(standard.getName()) && SPEC_OWNER.equals(standard.getOwner())) {
                        result = standard.getVersion();
                        break;
                    }
                }
            }
        }
        return result;
    }



    @CheckForNull
    @SuppressWarnings("deprecation")  // "Tool" class used for interpreting Standard BOM v2 documents
    private String getStandardBomVersionFromTools()
    {
        String result = null;
        List<org.cyclonedx.model.Tool> tools = getMetadata().getTools();
        if (tools != null) {
            for (org.cyclonedx.model.Tool tool : tools) {
                if ("standard-bom".equals(tool.getName()) && SPEC_OWNER.equals(tool.getVendor())) {
                    result = tool.getVersion();
                    break;
                }
            }
        }
        return result;
    }



    @Nonnull
    public Bom getCycloneDxBom()
    {
        return cycloneDxSbom;
    }
}
