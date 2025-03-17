/*
 * Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import org.cyclonedx.Version;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.parsers.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.sbom.standardbom.internal.CustomPropertySorter;
import com.siemens.sbom.standardbom.model.BomEntry;
import com.siemens.sbom.standardbom.model.StandardBom;


/**
 * Default serializer/deserializer for standard-bom files.
 */
public class StandardBomParser
{
    private static final Logger LOG = LoggerFactory.getLogger(StandardBomParser.class);

    private static final int BUFFER_SIZE_BYTES = 8192;

    private static final String NEWLINE_TOKEN = "§LINEBREAK§";



    /**
     * Parse the given file as a Standard BOM.
     *
     * @param pJsonFile the file to read
     * @return the parsed content as our DTOs which wrap CycloneDX DTOs
     *
     * @throws java.io.FileNotFoundException the given file was not found
     * @throws StandardBomException missing, unsupported, or invalid format version
     * @throws ParseException syntax error in JSON
     * @throws IOException I/O error reading the file
     */
    public StandardBom parse(final File pJsonFile)
        throws IOException, ParseException
    {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing Standard BOM: " + pJsonFile);
        }

        try (
            InputStream fis = new FileInputStream(pJsonFile);
            BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE_BYTES))
        {
            return parse(bis);
        }
    }



    /**
     * Parse the given file as a Standard BOM. Older format versions are converted into the current DTO format if
     * possible.
     *
     * @param pJsonStream an InputStream to read
     * @return the parsed content as current DTOs
     *
     * @throws java.io.FileNotFoundException the given file was not found
     * @throws StandardBomException missing, unsupported, or invalid format version
     * @throws ParseException syntax error in JSON
     * @throws IOException I/O error reading the file
     */
    public StandardBom parse(final InputStream pJsonStream)
        throws IOException, ParseException
    {
        final byte[] jsonBytes = toByteArray(pJsonStream);

        Parser jsonParser = BomParserFactory.createParser(jsonBytes);
        Bom bom = jsonParser.parse(jsonBytes);
        StandardBom result = new StandardBom(bom);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully parsed Standard BOM");
        }
        return result;
    }



    private byte[] toByteArray(final InputStream pJsonStream)
        throws IOException
    {
        final byte[] buffer = new byte[BUFFER_SIZE_BYTES];
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(42 * 1024);
        long count = 0;
        int n;
        while (-1 != (n = pJsonStream.read(buffer))) {
            baos.write(buffer, 0, n);
            count += n;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("toByteArray() - read " + count + " bytes from stream");
        }
        return baos.toByteArray();
    }



    /**
     * Store the given Standard BOM in a file.
     *
     * @param pBom the BOM to save
     * @param pOutputFile the file to write to
     * @throws StandardBomException serializing the given BOM failed
     * @throws IOException I/O error writing to the given file
     */
    public void save(@Nonnull final StandardBom pBom, @Nonnull final File pOutputFile)
        throws IOException
    {
        try {
            final byte[] jsonBytes = stringify(pBom).getBytes(StandardCharsets.UTF_8);
            Files.write(pOutputFile.toPath(), jsonBytes,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            LOG.debug("BOM stored in file: " + pOutputFile);
        }
        catch (StandardBomException e) {
            throw e;
        }
        catch (RuntimeException e) {
            throw new StandardBomException("Failed to write output file: " + pOutputFile, e);
        }
    }



    /**
     * Convert the given Standard BOM to a JSON String.
     *
     * @param pBom the BOM to convert. It will be modified slightly in the process, so do this last!
     * @return the String representation in JSON format
     *
     * @throws StandardBomException conversion failed
     */
    @Nonnull
    public String stringify(@Nonnull final StandardBom pBom)
    {
        // save the newlines in copyright statements and 3rd-party notices
        escapeComponentFields(pBom, BomEntry::getCopyright, BomEntry::setCopyright);
        escapeComponentFields(pBom, BomEntry::getThirdPartyNotices, BomEntry::setThirdPartyNotices);
        // allow purls as urls in external references (this is in fact legal, but the upstream serializer is too strict)
        escapeExtRefPurls(pBom);

        sortCustomProperties(pBom);

        String json = null;
        try {
            final Bom cycloneDxBom = pBom.getCycloneDxBom();
            BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_16, cycloneDxBom);
            json = generator.toJsonString();
        }
        catch (GeneratorException | RuntimeException e) {
            throw new StandardBomException("Failed to convert output to JSON", e);
        }

        json = unescapeComponentFields(json);
        json = unescapeExtRefPurls(json);
        json = removeEmptyServicesMetadata(json);
        json = injectSchemaSpec(json);
        return json.concat(System.lineSeparator());
    }



    @Nonnull
    private String injectSchemaSpec(@Nonnull final String pJson)
    {
        String schemaAttribute = "  \"$schema\": \"http://cyclonedx.org/schema/bom-1.6.schema.json\","
            .concat(System.lineSeparator());

        // Check if $schema attribute already exists
        if (pJson.contains("\"$schema\"")) {
            return pJson;
        }

        // Find the position right after the opening curly brace
        final String insertMarker = '{' + System.lineSeparator();
        int insertPosition = pJson.indexOf(insertMarker) + insertMarker.length();

        // Insert the $schema attribute at the found position
        StringBuilder sb = new StringBuilder(pJson);
        sb.insert(insertPosition, schemaAttribute);
        return sb.toString();
    }



    private void escapeExtRefPurls(@Nonnull final StandardBom pBom)
    {
        if (pBom.getCycloneDxBom().getExternalReferences() != null) {
            for (ExternalReference extRef : pBom.getCycloneDxBom().getExternalReferences()) {
                String purl = extRef.getUrl();
                if (purl != null && purl.startsWith("pkg:")) {
                    purl = "http://PURL-ESCAPE/" + purl.substring("pkg:".length());
                    extRef.setUrl(purl);
                }
            }
        }
    }



    private String unescapeExtRefPurls(@Nonnull final String pJson)
    {
        return pJson.replaceAll(Pattern.quote("http://PURL-ESCAPE/"), "pkg:");
    }



    private void escapeComponentFields(@Nonnull final StandardBom pBom,
        @Nonnull final Function<BomEntry, String> pGetter, @Nonnull final BiConsumer<BomEntry, String> pSetter)
    {
        for (BomEntry bomEntry : pBom.getComponents()) {
            String value = pGetter.apply(bomEntry);
            if (value != null) {
                value = value.replaceAll("\r?\n", NEWLINE_TOKEN);
                pSetter.accept(bomEntry, value);
            }
        }
    }



    @Nonnull
    private String unescapeComponentFields(@Nonnull final String pJson)
    {
        return pJson.replaceAll(NEWLINE_TOKEN, Matcher.quoteReplacement("\\n"));
    }



    @Nonnull
    private String removeEmptyServicesMetadata(@Nonnull final String pJson)
    {
        return pJson.replaceFirst(Pattern.quote("]," + System.lineSeparator() + "      \"services\" : [ ]"), "]");
    }



    private void sortCustomProperties(@Nonnull final StandardBom pBom)
    {
        pBom.getComponents().stream()
            .map(c -> c.getCycloneDxComponent().getProperties())
            .filter(Objects::nonNull)
            .forEach(customPropsList -> customPropsList.sort(CustomPropertySorter.INSTANCE));
    }
}
