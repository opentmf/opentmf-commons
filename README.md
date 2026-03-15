# opentmf-commons

General purpose utility classes and annotations for any Java project.

Compiled with **Java 17**. Uses **Jackson 3** and dependencies managed via **Spring Boot 4.0.x** BOM.

## Provided Utilities

Each public utility class and its public methods have Javadoc documentation. Below is a summary.

### JacksonUtil

Provides a pre-configured `ObjectMapper` and a rich set of static helper methods for JSON serialization, deserialization, and conversion.

Microservices that use this library are encouraged to build their own `ObjectMapper` via `defaultMapperBuilder()`, register any project-specific mix-ins, subtypes, or modules on the builder, and then call `setDefaultObjectMapper(mapper)` so that all `JacksonUtil` helper methods use the same customized instance.

**Key methods:**

| Method | Description |
|---|---|
| `defaultMapperBuilder()` | Returns a `JsonMapper.Builder` pre-configured with the opentmf defaults (NON_NULL, disabled timestamps, permissive OffsetDateTime deserializer, etc.). Customize and call `build()` to create your own mapper. |
| `setDefaultObjectMapper(ObjectMapper)` | Replaces the singleton used by all utility methods. Call once at startup after building a customized mapper. |
| `getDefaultObjectMapper()` | Returns the current singleton. **Do not mutate directly** — use the builder pattern above. |
| `jsonToObject` / `objectToJson` | JSON string ↔ object conversion. |
| `objectToPrettyJson` | Serializes to a pretty-printed JSON string (2-space indentation, LF line endings). |
| `objectToTree` / `jsonToTree` / `treeToObject` | Conversions between objects, JSON strings, and `JsonNode` trees. |
| `convertValue` | Object-to-object type conversion (e.g. `Map` to DTO). |
| `merge` | PATCH-style partial update — applies a JSON fragment onto an existing object. |
| `objectToMap` | Converts an object to an ordered `Map<String, Object>`. |
| `fileToObject` / `fileToTree` | Read JSON from classpath resources or files. |
| `jsonToTypeReference` / `jsonToMap` | Deserialize to generic types or maps. |
| `contents` / `inputStream` | Read classpath text resources. |

**Recommended usage in Spring Boot microservices:**

```java
@Configuration
public class JacksonConfig {

  @Primary
  @Bean
  public ObjectMapper objectMapper() {
    var builder = JacksonUtil.defaultMapperBuilder();

    // register your project-specific extensions
    Tmf641JacksonConfig.registerExtensions(builder);

    // build once, sync back to JacksonUtil
    var mapper = builder.build();
    JacksonUtil.setDefaultObjectMapper(mapper);
    return mapper;
  }
}
```

> **Note:** Call `build()` only once per builder. Each call to `defaultMapperBuilder()` returns a fresh builder.

### ValidationUtil

Provides on-demand Jakarta Bean Validation without requiring a Spring context.

- `validate(object)` — returns the set of constraint violations.
- `ensureValid(object)` — validates and throws a `ConstraintViolationException` with a descriptive message if any violations are found.

### ListUtil

Provides safe list constructors:
- `safe(list)` — returns an immutable copy (or empty list if null).
- `safeMutable(list)` — returns a mutable copy (or new empty list if null).

### PropertyUtil

Provides a utility method to find the environment variable name that overrides a given property.

### UrlUtil

Provides utility methods to construct, parse, or ensure HTTP URLs.

### Bean Validation Annotations

#### `@Required`

Class-level NotNull annotation for multiple fields.

- Validates only when the `@Required` annotation belongs to the actual runtime class or one of its directly implemented interfaces — not to a parent class. This allows each level in a class hierarchy to define its own set of required fields independently.
- Resolves `@JsonProperty` names from fields, interface getters (including `isXxx()` for booleans), and parent interface getters.

#### `@SafeText`

Allows only certain safe characters within a text field to defend against code injection attacks. Silently passes for non-`CharSequence` types (e.g. `Object` fields).

The allowed characters are:
- Alphanumeric characters
- Minus (`-`), Plus (`+`), Percent (`%`)
- Space, Asterisk (`*`), Slash (`/`)
- Dot (`.`), Colon (`:`), Underscore (`_`)

#### `@SafeId`

Allows only safe identifier characters:
- Alphanumeric characters
- Minus (`-`), Underscore (`_`)

#### `@SafeJsonPath`

Allows only characters valid in JSONPath expressions.

Allowed regex: `^[\w-+%$'~\[\](|):,?<>=&!@*./ ]*$`

#### `@SafeQuery`

Allows only safe query string characters:
- Alphanumeric characters
- Equals (`=`), Minus (`-`), Plus (`+`)
- Space, Asterisk (`*`), Dot (`.`)
- Underscore (`_`), At sign (`@`), Ampersand (`&`)

## Usage

### Maven Dependency

Many opentmf libraries depend on this base library. In most cases it is not necessary to explicitly include the dependency.

If you want to depend on it directly, the recommended way is to first import the managed dependency versions:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.opentmf</groupId>
      <artifactId>opentmf-versions</artifactId>
      <type>pom</type>
      <scope>import</scope>
      <version>RELEASE</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

> Instead of `RELEASE`, you may want to specify a fixed version number for build reproducibility.

Then add the dependency without a version:

```xml
<dependency>
  <groupId>org.opentmf.commons</groupId>
  <artifactId>opentmf-commons</artifactId>
</dependency>
```

You can also specify the version explicitly, bypassing the BOM import.

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for the full version history.
