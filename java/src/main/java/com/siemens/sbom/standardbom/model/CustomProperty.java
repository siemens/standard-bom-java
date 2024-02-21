/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

/**
 * The names of custom properties that we support in CycloneDX Components as per the
 * <a href="https://github.com/siemens/cyclonedx-property-taxonomy">Siemens Property Taxonomy</a> for CycloneDX.
 */
public final class CustomProperty
{
    /**
     * Flag indicating whether the component is a direct dependency ({@code true}) or a transitive dependency
     * ({@code false}).
     */
    public static final String DIRECT_DEPENDENCY = "direct";

    /**
     * The simple file name of the component binary (here, in Java context, the name of the JAR file) without path.
     */
    public static final String FILENAME = "filename";

    /** Pass-through legal text. */
    public static final String LEGAL_REMARK = "legalRemark";

    /** The primary programming language in which the component is written. Here, almost always {@code "Java"}. */
    public static final String PRIMARY_LANGUAGE = "primaryLanguage";

    /**
     * The <a href="https://sbom.siemens.io/latest/profiles.html">Standard BOM Profile</a> declared for this SBOM.
     * <p>Used on SBOM level ({@code metadata/properties}).</p>
     */
    public static final String PROFILE = "profile";

    public static final String THIRD_PARTY_NOTICES = "thirdPartyNotices";

    /**
     * A flag ({@code true} or {@code false}) indicating whether the Git workspace was clean when the SBOM was created,
     * i.e&#46; all changes had been committed. <p>Used on SBOM level, when describing the component for which the SBOM
     * is created.</p>
     */
    public static final String VCS_CLEAN = "vcsClean";

    /**
     * The most recent VCS hash, for example a Git commit hash. Together with {@link #VCS_CLEAN}, this additional value
     * allows ensuring accurate reproducibility of the SBOM. <p>Used on SBOM level, when describing the component for
     * which the SBOM is created.</p>
     */
    public static final String VCS_REVISION = "vcsRevision";



    private CustomProperty()
    {
        // constants class
    }
}
