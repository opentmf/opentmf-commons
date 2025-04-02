package org.opentmf.commons.util.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abdullah Beker
 */
@Getter
@Setter
@Entity
@Table
public class Classroom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String buildingName;
  private String buildingCode;
  private int floor;
  private String doorCode;

  @OneToOne(mappedBy = "classroom")
  private Professor professor;
}
