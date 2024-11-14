# Standard BOM for Java

A Java library for creating and consuming documents in
[standard-bom format](https://sbom.siemens.io/latest/format.html).


## Build script config

In order to use the Java library in your project, add it to your Gradle or Maven build.

TODO mention MavenCentral when publishing there is set up

**build.gradle**
```groovy
dependencies {
    implementation 'com.siemens.sbom.standardbom:standard-bom:4.1.0'
}
```


## Usage

The API is described in detail in the
[Javadoc documentation](https://sbom.siemens.io/javadoc/latest/).

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

2. **Gradle environment variable** - `GRADLE_USER_HOME` should be set globally to a directory like
   C:/Users/xxxx/.gradle (`xxxx` being your user ID; you might want to use backslashes on Windows). Create the
   directory if it doesn't exist.  
   Note that you *must not* install Gradle manually. If you have a manual Gradle installation on your machine, remove
   it.

Run the build by executing

```
./gradlew clean build
```

Publish a new version of this library by pushing a protected version tag in the format `vX.Y.Z`. The CI jobs will do
the rest.
