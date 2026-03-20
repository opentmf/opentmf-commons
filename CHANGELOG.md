# Changelog

All notable changes to this project will be documented in this file.

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
