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

  @JoinColumn
  @ManyToOne(fetch = FetchType.LAZY)
  public Professor professor;
}
