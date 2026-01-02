/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Hash;
import org.cyclonedx.model.Property;


/**
 * Helper class to handle the maps which CycloneDX 1.4 confusingly implemented as lists.
 *
 * @param <T> the type of element in the list
 */
public class ListProcessor<T>
{
    public static final ListProcessor<Hash> HASH = new ListProcessor<>(Hash::getAlgorithm, Hash::getValue);

    public static final ListProcessor<Property> PROPERTY = new ListProcessor<>(Property::getName, Property::getValue);

    private final Function<T, String> keySelector;

    private final Function<T, String> valueSelector;



    public ListProcessor(@Nonnull final Function<T, String> pKeySelector,
        @Nonnull final Function<T, String> pValueSelector)
    {
        keySelector = Objects.requireNonNull(pKeySelector, "key selector is required");
        valueSelector = Objects.requireNonNull(pValueSelector, "value selector is required");
    }



    @CheckForNull
    public String first(@Nullable final List<T> pList, @Nonnull final String pKey)
    {
        String result = null;
        if (pList != null) {
            T elem = pList.stream().filter(t -> pKey.equals(keySelector.apply(t))).findFirst().orElse(null);
            if (elem != null) {
                result = valueSelector.apply(elem);
            }
        }
        return result;
    }



    public void removeAll(@Nullable final List<T> pList, @Nonnull final String pKey)
    {
        if (pList != null) {
            pList.removeIf(elem -> pKey.equals(keySelector.apply(elem)));
        }
    }
}
