package com.pia.tmf.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * A period of time, either as a deadline (endDateTime only) a startDateTime only, or both.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
public class TimePeriod {

  private OffsetDateTime endDateTime;
  private OffsetDateTime startDateTime;
}
