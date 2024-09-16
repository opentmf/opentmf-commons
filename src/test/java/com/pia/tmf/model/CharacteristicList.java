package com.pia.tmf.model;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A characteristic list container for use with validation tests.
 *
 * @author Gokhan Demir
 */
@Getter
@Setter
public class CharacteristicList {

  @Valid private List<Characteristic> characteristics;
}
