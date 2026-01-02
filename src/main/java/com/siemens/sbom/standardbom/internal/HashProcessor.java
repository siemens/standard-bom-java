/*
 * Copyright (c) Siemens AG 2019-2026 ALL RIGHTS RESERVED
 *
 * SPDX-License-Identifier: MIT
 */
package com.siemens.sbom.standardbom.internal;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.cyclonedx.model.Hash;


/**
 * Encapsulates the implementations of getters and setters on our model classes which handle hash codes.
 */
public class HashProcessor
{
    private static final Map<Hash.Algorithm, Pattern> PATTERNS = buildPatterns();

    private final Supplier<List<Hash>> hashesSupplier;

    private final Consumer<Hash> hashAdder;



    public HashProcessor(@Nonnull final Supplier<List<Hash>> pHashesSupplier,
        @Nonnull final Consumer<Hash> pAdder)
    {
        hashesSupplier = Objects.requireNonNull(pHashesSupplier, "hashes supplier is required");
        hashAdder = Objects.requireNonNull(pAdder, "hashes adder is required");
    }



    @Nonnull
    @SuppressWarnings("RegExpSimplifiable")
    private static Map<Hash.Algorithm, Pattern> buildPatterns()
    {
        Map<Hash.Algorithm, Pattern> result = new EnumMap<>(Hash.Algorithm.class);
        result.put(Hash.Algorithm.MD5, Pattern.compile("[A-Fa-f0-9]{32}"));
        result.put(Hash.Algorithm.SHA1, Pattern.compile("[A-Fa-f0-9]{40}"));
        result.put(Hash.Algorithm.SHA_256, Pattern.compile("[A-Fa-f0-9]{64}"));
        result.put(Hash.Algorithm.SHA_512, Pattern.compile("[A-Fa-f0-9]{128}"));
        return result;
    }



    @CheckForNull
    public String get(@Nonnull final Hash.Algorithm pAlgorithm)
    {
        return ListProcessor.HASH.first(hashesSupplier.get(), pAlgorithm.getSpec());
    }



    public void set(@Nonnull final Hash.Algorithm pAlgorithm, @Nullable final String pHash)
    {
        ListProcessor.HASH.removeAll(hashesSupplier.get(), pAlgorithm.getSpec());
        if (pHash != null) {
            if (PATTERNS.containsKey(pAlgorithm) && !PATTERNS.get(pAlgorithm).matcher(pHash).matches()) {
                throw new IllegalArgumentException("value is not a valid " + pAlgorithm.getSpec() + " hash: " + pHash);
            }
            hashAdder.accept(new Hash(pAlgorithm, pHash.toLowerCase(Locale.ROOT)));
        }
    }
}
