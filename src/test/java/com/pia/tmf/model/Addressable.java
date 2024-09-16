package com.pia.tmf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pia.commons.validation.constraints.Required;
import com.pia.commons.validation.constraints.SafeText;
import jakarta.validation.constraints.Size;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
@Required(fields = {"id"})
public class Addressable {

  @SafeText
  @Size(max = 50)
  @JsonProperty("id")
  private String id;

  private URI href;
}
