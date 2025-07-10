[![build](https://github.com/siemens/standard-bom-java/actions/workflows/build.yml/badge.svg)](https://github.com/siemens/standard-bom-java/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/siemens/standard-bom-java/graph/badge.svg?token=03UC0U5M10)](https://codecov.io/gh/siemens/standard-bom-java)
[![Standard BOM](https://img.shields.io/badge/Standard%20BOM-v3.0.0-1f425f.svg)](https://code.siemens.com/sbom/standard-bom/-/releases/v3.0.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.siemens.sbom.standardbom/standard-bom)](https://central.sonatype.com/artifact/com.siemens.sbom.standardbom/standard-bom)


# Standard BOM for Java

A Java library for creating and consuming documents in
[standard-bom format](https://sbom.siemens.io/latest/format.html). "Standard BOM" is our Siemens-internal SBOM
format. Every Standard BOM document is a 100% [CycloneDX](https://cyclonedx.org/) document, so consumers who do not
know about Standard BOM can just treat it as CycloneDX and be happy.


## Build script config

In order to use the Java library in your project, add it to your Gradle or Maven build. We publish to Maven Central,
so no special configuration is needed.

**Gradle**
```groovy
dependencies {
    implementation 'com.siemens.sbom.standardbom:standard-bom:4.2.0'
}
```

**Maven**
```xml
<dependency>
    <groupId>com.siemens.sbom.standardbom</groupId>
    <artifactId>standard-bom</artifactId>
    <version>4.2.0</version>
</dependency>
```


## Usage

The API is described in detail in the
[Javadoc documentation](https://siemens.github.io/standard-bom-java/latest/).

Here are some examples:

- Read a Standard BOM from an input stream:
  ```java
  StandardBom bom = null;
  try (InputStream is = getClass().getResourceAsStream("standard-bom.json")) {
      bom = new StandardBomParser().parse(is);
  }
  ```
- Read a Standard BOM from a file:
  ```java
  File bomFile = new File("/path/to/standard-bom.json");
  StandardBom bom = new StandardBomParser().parse(bomFile);
  ```

The `StandardBomParser` will handle the format of the JSON file. If it is older than the current format, it will
convert it to the current DTOs. Using the `StandardBomParser`, you always get the current DTOs. If the input file is
too old, a `StandardBomException` will result.

- Write a Standard BOM to a file:
  ```java
  StandardBom bom = ...;
  File outputFile = new File("/path/to/standard-bom.json");
  new StandardBomParser().save(bom, outputFile);
  ```
- Write a Standard BOM to a String:
  ```java
  StandardBom bom = ...;
  String json = new StandardBomParser().stringify(bom);
  ```

The output format will always be the current version of the format.


## Development

In order to build this library on your local PC, and/or contribute to this library, mind the following prerequisites:

1. **Java** - make sure [JDK 8](https://adoptium.net/temurin/releases/?version=8) is available on your system, and
   the `JAVA_HOME` environment variable points to it.

2. **Gradle** - We use the Gradle Wrapper, so there is nothing to install.

Run the build by executing

```
./gradlew clean build
```


## License

The Standard BOM library for Java is Open Source under the [MIT license](LICENSE) (SPDX-License-Identifier: MIT).

Copyright (c) Siemens AG 2019-2025 ALL RIGHTS RESERVED
