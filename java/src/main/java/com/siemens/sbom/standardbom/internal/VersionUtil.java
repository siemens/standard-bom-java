/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.siemens.sbom.standardbom.StandardBomException;


/**
 * Provides the content of the <code>standard-bom-version.properties</code>.
 */
public final class VersionUtil
{
    private static final String VERSION_FILENAME = "/standard-bom-version.properties";

    private static final Properties VERSION_PROPS = readProperties(VERSION_FILENAME);



    private VersionUtil()
    {
        // do nothing
    }



    static Properties readProperties(final String pVersionFile)
    {
        final Properties props = new Properties();
        try (InputStream is = VersionUtil.class.getResourceAsStream(pVersionFile)) {
            if (is == null) {
                throw new FileNotFoundException("Version file not found: " + pVersionFile);
            }
            props.load(is);
        }
        catch (IOException | RuntimeException e) {
            throw new StandardBomException("Bug: Failed to load version information file: " + pVersionFile, e);
        }
        return props;
    }



    public static String getFormatVersion()
    {
        return VERSION_PROPS.getProperty("format");
    }



    public static String getLibraryVersion()
    {
        return VERSION_PROPS.getProperty("version");
    }



    public static String getWebsite()
    {
        return VERSION_PROPS.getProperty("website");
    }
}
