/*
 * Copyright (c) Siemens AG 2019-2024 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A helper class which allows serializing a {@code Map<String, String>} into a String.
 */
public class StringMapProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(StringMapProcessor.class);

    private final Supplier<String> stringSupplier;

    private final Consumer<String> stringChange;



    /**
     * Keys are always sorted for easier comparison of test data.
     */
    private static class SortedProperties
        extends Properties
    {
        @Nonnull
        @Override
        public synchronized Enumeration<Object> keys()
        {
            List<Object> keyList = Collections.list(super.keys());
            keyList.sort(Comparator.comparing(Object::toString));
            return Collections.enumeration(keyList);
        }



        @Nonnull
        @Override
        public Set<Map.Entry<Object, Object>> entrySet()
        {
            return Collections.synchronizedSet(super.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().toString()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
    }



    public StringMapProcessor(@Nonnull final Supplier<String> pStringSupplier,
        @Nonnull final Consumer<String> pStringChange)
    {
        stringSupplier = Objects.requireNonNull(pStringSupplier);
        stringChange = Objects.requireNonNull(pStringChange);
    }



    @Nonnull
    private Properties loadProps()
    {
        final Properties props = new SortedProperties();
        if (stringSupplier.get() != null) {
            try (StringReader sr = new StringReader(stringSupplier.get())) {
                props.load(sr);
            }
            catch (IOException | RuntimeException e) {
                LOG.warn("Comment is not recognized as encoded by standard-bom: " + stringSupplier.get());
            }
        }
        return props;
    }



    @CheckForNull
    private String serializeProps(@Nonnull final Properties pProps)
    {
        if (pProps.isEmpty()) {
            return null;
        }

        StringWriter sw = new StringWriter();
        try {
            pProps.store(sw, null);
        }
        catch (IOException | RuntimeException e) {
            throw new IllegalArgumentException("value fails to serialize", e);
        }
        String s = sw.toString();
        s = s.substring(s.indexOf('\n') + 1).replaceAll("\r", "");
        if (s.endsWith("\n")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }



    @CheckForNull
    public String get(@Nonnull final String pKey)
    {
        final Properties props = loadProps();
        return props.getProperty(pKey);
    }



    public void set(@Nonnull final String pKey, @Nullable final String pValue)
    {
        final Properties props = loadProps();
        props.remove(pKey);
        if (pValue != null && !pValue.trim().isEmpty()) {
            props.setProperty(pKey, pValue.trim());
        }
        stringChange.accept(serializeProps(props));
    }
}
