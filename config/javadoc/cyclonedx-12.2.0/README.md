# CycloneDX Javadoc Package List

`package-list` is copied from <https://javadoc.io/doc/org.cyclonedx/cyclonedx-core-java/12.2.0/element-list>.
This non-modular index contains only package names, so no conversion is needed beyond the filename.

Java 8 Javadoc requires `package-list` for external linking. Using this local index with `linksOffline` restores
CycloneDX type links while retaining our Java 8 documentation format for downstream consumers. It does not translate
Java 8 method anchors to the newer format used by CycloneDX documentation.

When upgrading CycloneDX, refresh the index from the matching release and update the directory and documentation URL
in `build.gradle` together with the dependency.
