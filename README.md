# opentmf-commons

General purpose utility classes and annotations for any Java project.

Compiled with **Java 17**. Uses **Jackson 3** and dependencies managed via **Spring Boot 4.0.x** BOM.

## Provided Utilities

Each public utility class and its public methods have Javadoc documentation. Below is a summary.

### JacksonUtil

Provides a pre-configured `ObjectMapper` and a rich set of static helper methods for JSON serialization, deserialization, and conversion.

Microservices that use this library are encouraged to build their own `JsonMapper` via `defaultMapperBuilder()`, register any project-specific mix-ins, subtypes, or modules on the builder, and then call `setDefaultJsonMapper(mapper)` so that all `JacksonUtil` helper methods use the same customized instance.

**Key methods:**

| Method | Description |
|---|---|
| `defaultMapperBuilder()` | Returns a `JsonMapper.Builder` pre-configured with the opentmf defaults (NON_NULL, permissive OffsetDateTime deserializer, etc.). Customize and call `build()` to create your own mapper. |
| `setDefaultJsonMapper(JsonMapper)` | Replaces the singleton used by all utility methods. Call once at startup after building a customized mapper. |
| `getDefaultJsonMapper()` | Returns the current singleton. **Do not mutate directly** — use the builder pattern above. |
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
  public JsonMapper jsonMapper() {
    var builder = JacksonUtil.defaultMapperBuilder();

    // register your project-specific extensions
    Tmf641JacksonConfig.registerExtensions(builder);

    // build once, sync back to JacksonUtil
    var mapper = builder.build();
    JacksonUtil.setDefaultJsonMapper(mapper);
    return mapper;
  }
}
```

> **Note:** Call `build()` only once per builder. Each call to `defaultMapperBuilder()` returns a fresh builder.

> **Spring Boot note:** A mapper built via `defaultMapperBuilder()` does not inherit settings from `spring.jackson.*` properties. This is intentional — the builder provides a consistent baseline across all microservices that use this library. Returning `JsonMapper` (not `ObjectMapper`) ensures Spring Boot 4 detects the bean and does not auto-configure a second mapper.

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

All `Safe*` constraints are **defense-in-depth** measures: parameterized queries (against SQL injection), structured parsing (for filters, JsonPath, URLs), and contextual output encoding (against XSS) must still be applied at the boundaries that consume the validated values.

#### `@SafeText`

Allows only certain safe characters within a text field to defend against code injection attacks. Silently passes for non-`CharSequence` types (e.g. `Object` fields). Internationalized — accepts names like `François`, `O'Brien`, and `l’Hôpital`.

The allowed characters are:
- Unicode letters and combining marks (`\p{L}`, `\p{M}`)
- Unicode digits (`\p{N}`)
- Space and Underscore (`_`)
- Apostrophe (`'`) and right single quotation mark (`’`)
- Minus (`-`), Plus (`+`)
- Percent (`%`), Asterisk (`*`)
- Dot (`.`), Comma (`,`), Colon (`:`)
- Slash (`/`)
- Question mark (`?`), Exclamation mark (`!`)
- Parentheses (`(` and `)`)

Angle brackets, double quotes, backticks, semicolons, equals signs, braces, square brackets, backslash, pipe, dollar, hash, caret, tilde, at-sign, and control characters are rejected.

#### `@SafeId`

Allows only ASCII-safe identifier characters. Intended for opaque identifiers such as UUIDs, slugs, and sequence numbers; for human-readable text use `@SafeText`.

The allowed characters are:
- ASCII letters (A–Z, a–z) and digits (0–9)
- Underscore (`_`), Minus (`-`)

The empty string is accepted; combine with `@Size(min = 1)` or `@NotBlank` to require presence.

#### `@SafeJsonPath`

Allows only characters valid in JSONPath expressions, with Unicode-aware key support so paths can reference non-Latin keys (e.g. `$.müşteri.ad`).

Allowed regex: `^[\p{L}\p{M}\p{N}_\-+%$'~\[\](|):,?<>=&!@*./ ]*$`

This is a defense-in-depth pre-filter; the value should still be parsed by a real JsonPath engine (e.g. Jayway's `JsonPath.compile(...)`) before use.

#### `@SafeUrl`

Validates that the annotated value is a syntactically valid URL or relative URI reference, suitable for TMF `href`, callback, and link fields. Validation is performed by **parsing the value with `java.net.URI`**, not by regex character-class matching.

Accepts:
- Absolute URLs with `http` or `https` scheme (e.g. `https://api.example.com/v1/customer/123`)
- Relative URI references (e.g. `/customer/123`, `customer/123?expand=foo`, `../sibling/123`, `#fragment-only`)
- `null` and the empty string (combine with `@NotNull` / `@Size(min = 1)` / `@NotBlank` to require presence)

Rejects:
- Schemes other than `http`/`https` — including `javascript:`, `data:`, `file:`, `ftp:`, `mailto:`
- Absolute URLs with no host
- Strings containing ASCII control characters (CR/LF used for header injection, etc.)
- Anything that fails `URI` parsing

For stricter absolute-only validation (require https, pin a specific host, …) prefer `@org.hibernate.validator.constraints.URL` from Hibernate Validator — note however that `@URL` does **not** accept relative references, so it cannot replace `@SafeUrl` on fields that may hold either form.

#### `@SafeQuery` _(deprecated)_

Deprecated since 2.2.0, for removal. Retained as a meta-composed synonym for `@SafeUrl` so existing call sites keep working without behavior change. New code should use `@SafeUrl` directly. Migration is a search-and-replace.

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
