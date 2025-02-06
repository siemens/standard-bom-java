/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;


/**
 * Helper class which adds or removes the {@code file:} protocol to convert between a relative path and an URL.
 */
public final class FileProtocolHandler
{
    private static final Pattern FILE_PROT_PATTERN = Pattern.compile("file:/{0,3}(.+)", Pattern.CASE_INSENSITIVE);



    private FileProtocolHandler()
    {
        // utility class
    }



    @CheckForNull
    public static String ensureFileProtocol(@Nullable final String pRelativePath)
    {
        if (pRelativePath != null) {
            Matcher m = FILE_PROT_PATTERN.matcher(pRelativePath);
            if (!m.matches()) {
                return "file:///" + pRelativePath;
            }
        }
        return pRelativePath;
    }



    @CheckForNull
    public static String withoutFileProtocol(@Nullable final String pRelativePath)
    {
        if (pRelativePath != null) {
            Matcher m = FILE_PROT_PATTERN.matcher(pRelativePath);
            if (m.matches()) {
                return m.group(1);
            }
        }
        return pRelativePath;
    }
}
