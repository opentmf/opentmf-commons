package com.pia.commons.util.repository.entity;

import jakarta.persistence.*;
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

  @JoinColumns({
    @JoinColumn(name = "professor_name", referencedColumnName = "name"),
    @JoinColumn(name = "professor_surname", referencedColumnName = "surname")
  })
  @ManyToOne(fetch = FetchType.LAZY)
  public Professor professor;
}
