package org.opentmf.tmf.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.opentmf.commons.validation.constraints.Required;
import org.opentmf.commons.validation.constraints.SafeText;

/**
 * Another Characteristic that is related to the current Characteristic.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
@Required(fields = {"relationshipType"})
public class CharacteristicRelationship extends Entity {

  @SafeText
  @Size(min = 1, max = 255)
  private String relationshipType;
}
