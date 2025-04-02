package org.opentmf.tmf.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opentmf.commons.validation.constraints.Required;
import org.opentmf.commons.validation.constraints.SafeText;

/**
 * Describes a given characteristic of an object or entity through a name/value pair.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
@NoArgsConstructor
@Required(fields = {"name", "value"})
public class Characteristic extends Entity {

  @NotEmpty
  @SafeText
  @Size(max = 255)
  private String name;

  @SafeText
  @Size(min = 1, max = 255)
  private String valueType;

  @NotNull
  @SafeText
  private Object value;

  @Valid
  private List<CharacteristicRelationship> characteristicRelationship;
}
