package com.pia.commons.util.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Abdullah Beker
 */
@Getter
@Setter
@Entity
@Table
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int credits;
  private String name;

  @ManyToMany(mappedBy = "courses")
  private List<Professor> professors;
}
