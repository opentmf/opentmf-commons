# pia-commons
General purpose utility classes and annotations for any Java project.

## Provided Utilities
Below are the provided utility classes within this library. Note that, each public utility class and their public methods have Javadoc documentation. Here is the summary of the utility classes and their abilities:

### JacksonUtil
- Provides a singleton ObjectMapper instance.
- Provides many utility methods that uses the singleton ObjectMapper instance. That means, if applications expose an ObjectMapper bean themselves, -which should be the case for many applications- they can use the singleton ObjectMapper to further customize it, hence, not losing the ability to use the provided utility methods the way they configured their own ObjectMapper bean.
- JacksonUtil is a very important class and is used by many pia-commons libraries.

### ValidationUtil
- Provides on-demand Java (Jakarta) Bean validation

### ListUtil
- Provides safe list constructors.

### PropertyUtil
- Provides a utility method to find environment variable name that overrides a property.

### UrlUtil
- Provides useful utility methods to construct, parse or ensure Http URLs.

### Additional Bean Validation Annotations

#### `@Required`
- Class level NotNull annotation for multiple fields.
- Validates only and only if, at the time of the validation, the initialized @Required belongs to the actual declaring class itself, not to a parent class.

#### `@SafeText`
- Allows only certain safe characters within the text field to defend against potential code injection attacks. 
- The allowed characters are:
  - Alphanumeric characters
  - Minus (-)
  - Plus (+)
  - Space ( )
  - Asterisk (*)
  - Slash (/)
  - Dot (.)
  - Colon (:)
  - Underscore (_)

#### `@SafeId`
- Allows only certain safe characters within the text field to defend against potential code injection attacks.
- The allowed characters are:
  - Alphanumeric characters
  - Minus (-)
  - Underscore (_)

#### `@SafeJsonPath`
- Allows only certain safe characters to exist in a jsonPath string.
- The allowed regex is:
  - `Pattern.compile("^[\\w-+%$'~\\[\\](|):,?<>=&!@*./ ]*$")`

#### `@SafeQuery`
- Allows only certain safe characters to exist in a query string. 
- The allowed characters are:
  - Alphanumeric characters
  - Equals (=)
  - Minus (-)
  - Plus (+)
  - Space ( )
  - Asterisk (*)
  - Dot (.)
  - Underscore (_)
  - Ampersand (&) Usage

## Usage

### Maven Dependency
Many pia-commons libraries depend on this base library. In most cases not necessary to explicitly include the dependency. 

In any way, if you want to directly depend on this utility, the best way is, after importing the managed dependencies of the pia-commons repositories: 

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.pia.commons</groupId>
      <artifactId>pia-commons-versions</artifactId>
      <type>pom</type>
      <scope>import</scope>
      <version>RELEASE</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```
> Note that instead of `RELEASE`, you might want to specify a static version number to retain build predictability for the future.

And then we will be able to depend on this pia-commons library without specifying a version.
```xml
<dependency>
  <groupId>com.pia.commons</groupId>
  <artifactId>pia-commons</artifactId>
</dependency>
```
Of course, you can opt to specify a version number explicitly as well, bypassing the above dependency import section.

## Version History
### 1.0.0
- Initial Version
### 1.0.1
- Fixes OffsetDateTime deserializer to prevent losing the original timezone designator.
- Updates to Spring Boot 3.4.1
- Adds FieldSelectionUtil.
