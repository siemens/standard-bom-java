/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.internal;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.ExternalReference;


/**
 * Helper class which processes external references in a CycloneDX Component.
 */
public class ExtRefProcessor
{
    private final Supplier<List<ExternalReference>> extRefSupplier;

    private final Consumer<ExternalReference> extRefAdder;



    public ExtRefProcessor(@Nonnull final Supplier<List<ExternalReference>> pExtRefSupplier,
        @Nonnull final Consumer<ExternalReference> pExtRefAdder)
    {
        extRefSupplier = Objects.requireNonNull(pExtRefSupplier, "external references list supplier was null");
        extRefAdder = Objects.requireNonNull(pExtRefAdder, "external references adder was null");
    }



    @CheckForNull
    public String get(@Nonnull final Predicate<ExternalReference> pPredicate)
    {
        if (extRefSupplier.get() != null) {
            ExternalReference ref = extRefSupplier.get().stream().filter(pPredicate).findFirst().orElse(null);
            if (ref != null) {
                return ref.getUrl();
            }
        }
        return null;
    }



    public void set(@Nonnull final Predicate<ExternalReference> pPredicate, @Nullable final String pUrl,
        @Nonnull final Consumer<ExternalReference> pSetValue)
    {
        if (extRefSupplier.get() != null) {
            extRefSupplier.get().removeIf(pPredicate);
        }

        if (pUrl != null && !pUrl.trim().isEmpty()) {
            final ExternalReference extRef = new ExternalReference();
            extRef.setUrl(pUrl.trim());
            pSetValue.accept(extRef);
            extRefAdder.accept(extRef);
        }
    }
}
