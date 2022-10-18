/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.scp.standardbom.model;

/**
 * The names of custom properties that we support in CycloneDX Components.
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

    /** The primary programming language in which the component is written. Here, almost always {@code "Java"} . */
    public static final String PRIMARY_LANGUAGE = "primaryLanguage";

    public static final String THIRD_PARTY_NOTICES = "thirdPartyNotices";



    private CustomProperty()
    {
        // constants class
    }
}
