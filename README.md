# pia-commons
General purpose utility classes and annotations for any Java project.

## Provided Utilities
Below are the provided utility classes within this library. Note that, each public utility class and their public methods have Javadoc documentation. Here is the summary of the utility classes and their abilities:

### JacksonUtil
- Provides a singleton ObjectMapper instance.
- Provides many utility methods that uses the singleton ObjectMapper instance. That means, if applications expose an ObjectMapper bean themselves, -which should be the case for many applications- they can use the singleton ObjectMapper to further customize it, hence, not losing the ability to use the provided utility methods the way they configured their own ObjectMapper bean.
- JacksonUtil is a very important class and is used by many pia-commons libraries.

### FieldsSelectionUtil
- A utility for dynamically selecting and extracting specific fields from a list of objects.
- This utility also handles the lazy-collections if the bean is a JPA entity and the JPA EntityManager is not closed.
- It handles EmbeddedId fields by excluding the embeddedId field name. or example, if the embeddedId field name is id, and it contains field1 and field2, instead of exposing them as id.field1 and id.field2, it exposes them as field1, field2 by default. 

#### Selecting All Fields
```java
List<MyEntity> entities = repository.findAll();
List<Map<String, Object>> results = FieldSelectionUtil.fieldsToMapList(entities);
```
Output example:
```json
[
  { "id": 1, "name": "John", "surname": "Doe" },
  { "id": 2, "name": "Jane", "surname": "Doe" }
]
```

#### Selecting Specific Fields
```java
String fields = "id,name";
List<Map<String, Object>> results = FieldSelectionUtil.fieldsToMapList(entities, fields);
```
Output example:
```json
[
  { "id": 1, "name": "John" },
  { "id": 2, "name": "Jane" }
]
```

#### Selecting Nested Fields
You can specify as many nested fields as you want, however the maximum supported depth is 10.

```java
String fields = "id,classroom.id,classroom.buildingName";
List<Map<String, Object>> results = FieldSelectionUtil.fieldsToMapList(entities, fields);
```
Output example:
```json
[
  {
    "id": 1,
    "classroom": {
      "id": 101,
      "buildingName": "Main Building"
    }
  }
]
```

#### Field Selection Rules
1. **Comma-separated Fields:** Specify fields using commas, e.g., `id,name`.
2. **Nested Fields:** Use dot notation for nested fields, e.g., `classroom.id,classroom.professor.name`.
3. **Collections:** Automatically resolves nested collections, e.g., `students.id` retrieves IDs from each student.

---

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
### 1.0.2
- FieldSelectionUtil has been moved to com.pia.commons.util.fieldselection package.
- FieldSelectionUtil now handles EmbeddedId fields. Instead of id.field1, id.field2, it exposes them as field1, field2 by default.  
