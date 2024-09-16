package com.pia.tmf.model;

import com.pia.commons.validation.constraints.SafeQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

/**
 * Sets the communication endpoint address the service instance must use to deliver notification
 * information.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
public class EventSubscriptionInput {

  @NotNull
  private URI callback;

  @SafeQuery
  @Size(max = 500)
  private String query;
}
