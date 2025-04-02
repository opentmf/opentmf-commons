package org.opentmf.tmf.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.opentmf.commons.validation.constraints.SafeText;

/**
 * Entity reference schema to be use for all entityRef class.
 */
@Getter
@Setter
public class EntityRef extends Entity {

  @SafeText
  @Size(min = 1, max = 255)
  private String name;

  @SafeText
  @Size(min = 1, max = 255)
  @JsonProperty("@referredType")
  private String atReferredType;
}

