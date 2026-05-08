# Changelog

All notable changes to this project will be documented in this file.

## 2.2.0 - 2026-05-08

### Changed
- Relaxed `@SafeText` to accept Unicode letters/marks/digits and a wider set of common
  punctuation (apostrophe, comma, parentheses, `?`, `!`). International names such as
  `François`, `O'Brien`, and `l’Hôpital` now validate. Characters commonly used in SQL
  or script-injection payloads — angle brackets, double quotes, backticks, semicolons,
  equals signs, braces, square brackets, backslash, pipe, dollar, hash, caret, tilde,
  at-sign, and control characters — remain rejected.
- Made `@SafeJsonPath` Unicode-aware so paths can reference non-Latin keys
  (e.g. `$.müşteri.ad`). The character set is otherwise unchanged.
- Documentation: enumerated the allowed characters in `@SafeJsonPath`'s JavaDoc,
  added defense-in-depth notes to the `Safe*` annotations, and clarified in
  `@SafeId`'s JavaDoc that the empty string is accepted (combine with `@Size(min = 1)`
  or `@NotBlank` if presence is required).
- The `Safe*` constraints remain defense-in-depth measures: parameterized queries,
  structured filter parsing, and contextual output encoding are still required at the
  boundaries that consume the validated values.

### Added
- `@SafeUrl` — validates that a value is a syntactically valid URL or relative URI
  reference, suitable for TMF `href`, callback, and link fields. Backed by
  `java.net.URI` parsing rather than regex character-class matching, so both absolute
  URLs (`https://api.example.com/v1/customer/123`) and relative references
  (`/customer/123`, `customer/123?expand=foo`) are accepted. Absolute URLs are
  restricted to `http`/`https` schemes; `javascript:`, `data:`, `file:`, `ftp:`, and
  `mailto:` are rejected. ASCII control characters (CR/LF used for header injection)
  are rejected. For stricter absolute-only validation, prefer
  `@org.hibernate.validator.constraints.URL` from Hibernate Validator — note however
  that `@URL` does not accept relative references.

### Deprecated
- `@SafeQuery` is deprecated in favor of `@SafeUrl`. The annotation is retained as a
  meta-composed synonym with identical semantics, so existing call sites continue to
  work without behavior change; new code should use `@SafeUrl` directly. The original
  regex-based validator (`SafeQueryValidator`) and the `SAFE_QUERY` regex are removed
  — both are unreachable through the synonym composition. Migration is a
  search-and-replace.

## 2.1.0

### Breaking Changes
- Renamed `JacksonUtil.getDefaultObjectMapper()` → `getDefaultJsonMapper()` (returns `JsonMapper`).
- Renamed `JacksonUtil.setDefaultObjectMapper(ObjectMapper)` → `setDefaultJsonMapper(JsonMapper)`.

### Changed
- Introduced `release` Maven profile — source jar, javadoc jar, GPG signing, and central publishing now only run with `-Prelease`.
- Javadoc example `@Bean` method returns `JsonMapper` (not `ObjectMapper`) for correct Spring Boot 4 conditional bean detection.
- Added Spring Boot compatibility note to `defaultMapperBuilder()` javadoc.
- Rewritten README documentation.

## 2.0.1 - 2026-03-20

### Changed
- Updated Spring Boot dependencies to 4.0.4

## 2.0.0 - 2026-03-14

### Breaking Changes
- **Migrated from Jackson 2 to Jackson 3.** Jackson imports in downstream code that reference `com.fasterxml.jackson.core` or `com.fasterxml.jackson.databind` must be updated to `tools.jackson.core` / `tools.jackson.databind`. Annotation imports (`com.fasterxml.jackson.annotation.*`) remain unchanged.
- `JacksonException` now extends `RuntimeException` (no longer `IOException`). Utility methods still wrap Jackson exceptions in `IllegalArgumentException` for backward compatibility.
- Replaced `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` dependency with `tools.jackson.core:jackson-databind` (java.time support is now built into Jackson 3).
- Removed `FieldSelectionUtil` and the entire `org.opentmf.commons.util.fieldselection` package (moved to a separate library).
- Removed `commons-beanutils` and `jakarta.persistence-api` dependencies.
- Dependency management updated from Spring Boot 3.x to **Spring Boot 4.0.x** BOM.

### Added
- `JacksonUtil.defaultMapperBuilder()` returns a `JsonMapper.Builder` pre-configured with the opentmf defaults. This is the recommended way to create customized ObjectMapper instances, replacing direct mutation of the singleton.
- `JacksonUtil.setDefaultObjectMapper(ObjectMapper)` allows replacing the default ObjectMapper used by all utility methods.
- `JacksonUtil.convertValue(Object, Class<T>)` for object-to-object type conversion.
- `JacksonUtil.treeToObject(JsonNode, TypeReference<T>)` for converting a JsonNode to a generic type.
- `JacksonUtil.merge(T, String)` for PATCH-style partial updates on existing objects.
- `JacksonUtil.objectToMap(Object)` for converting an object to an ordered `Map<String, Object>`.
- `RequiredValidator` now resolves `@Required` annotations declared on interfaces.
- `RequiredValidator` now resolves `@JsonProperty` from getter methods on interfaces.

### Changed
- `JacksonUtil.OBJECT_MAPPER` is now `volatile` instead of `final`, allowing runtime replacement via `setDefaultObjectMapper`.
- `JacksonUtil.defaultMapperBuilder()` simplified — Jackson 3 defaults already disable `WRITE_DATES_AS_TIMESTAMPS`, `FAIL_ON_EMPTY_BEANS`, and `FAIL_ON_UNKNOWN_PROPERTIES`.
- `JacksonUtil.treeToObject(JsonNode, TypeReference)` now uses the native `treeToValue(TreeNode, TypeReference)` method added in Jackson 3.

## 1.0.7

- Fix: JacksonUtil's inputStream method now uses the current thread's classloader to load the classpath resource.

## 1.0.6

- Improvement: Handle also the numeric values in OffsetDateTime deserialization.

## 1.0.5

- Fix: Handles empty string and null values in OffsetDateTime deserialization.

## 1.0.4

- Initial central repository release.

## 1.0.3

- Preparations to move to central repository.

## 1.0.2

- FieldSelectionUtil has been moved to org.opentmf.commons.util.fieldselection package.
- FieldSelectionUtil now handles EmbeddedId fields.

## 1.0.1

- Fixes OffsetDateTime deserializer to prevent losing the original timezone designator.
- Updates to Spring Boot 3.4.1.
- Adds FieldSelectionUtil.

## 1.0.0

- Initial version.
