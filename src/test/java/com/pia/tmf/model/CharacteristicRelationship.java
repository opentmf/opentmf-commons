package com.pia.tmf.model;

import com.pia.commons.validation.constraints.Required;
import com.pia.commons.validation.constraints.SafeText;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
