package com.pia.tmf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pia.commons.validation.constraints.SafeText;
import jakarta.validation.constraints.Size;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

/**
 * Base entity schema for use in TMForum Open-APIs.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
public class Entity extends Addressable {

  @SafeText
  @Size(min = 1, max = 255)
  @JsonProperty("@baseType")
  private String atBaseType;

  @JsonProperty("@schemaLocation")
  private URI atSchemaLocation;

  @SafeText
  @Size(min = 1, max = 255)
  @JsonProperty("@type")
  private String atType;
}

