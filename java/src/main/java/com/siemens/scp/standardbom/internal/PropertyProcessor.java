/*
 * Copyright (c) Siemens AG 2019-2022 ALL RIGHTS RESERVED
 */
package com.siemens.scp.standardbom.internal;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Property;

import com.siemens.scp.standardbom.model.StandardBom;


/**
 * Helper class which processes custom properties in a CycloneDX Component.
 */
public class PropertyProcessor
{
    private final Supplier<List<Property>> propertySupplier;

    private final Consumer<Property> propertyAdder;



    public PropertyProcessor(@Nonnull final Supplier<List<Property>> pPropertySupplier,
        @Nonnull final Consumer<Property> pPropertyAdder)
    {
        propertySupplier = Objects.requireNonNull(pPropertySupplier, "property supplier was null");
        propertyAdder = Objects.requireNonNull(pPropertyAdder, "property adder was null");
    }



    @CheckForNull
    public String get(@Nonnull final String pKey)
    {
        return ListProcessor.PROPERTY.first(propertySupplier.get(), namespacedKey(pKey));
    }



    public void set(@Nonnull final String pKey, @Nullable final String pValue)
    {
        final String key = namespacedKey(pKey);
        ListProcessor.PROPERTY.removeAll(propertySupplier.get(), key);

        if (pValue != null && !pValue.trim().isEmpty()) {
            final Property prop = new Property();
            prop.setName(key);
            prop.setValue(pValue.trim());
            propertyAdder.accept(prop);
        }
    }



    @Nonnull
    private String namespacedKey(@Nonnull final String pKey)
    {
        String result = pKey;
        if (!pKey.contains(":")) {
            result = StandardBom.CUSTOM_PROPERTY_NAMESPACE + ":" + pKey;
        }
        return result;
    }
}
