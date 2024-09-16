package com.pia.tmf.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pia.commons.validation.constraints.SafeText;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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

