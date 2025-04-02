package org.opentmf.commons.util.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;
  private String surname;

  @JoinColumn(name = "professor_name", referencedColumnName = "name")
  @JoinColumn(name = "professor_surname", referencedColumnName = "surname")
  @ManyToOne(fetch = FetchType.LAZY)
  public Professor professor;
}
