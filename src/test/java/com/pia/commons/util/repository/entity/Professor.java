package com.pia.commons.util.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Abdullah Beker
 */
@Getter
@Setter
@Table
@Entity
public class Professor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;
  private String surname;
  private OffsetDateTime createdOn;

  @OneToMany(mappedBy = "professor")
  private List<Student> student;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Classroom classroom;

  @ManyToMany
  @JoinTable(
      name = "professor_course",
      joinColumns = @JoinColumn(name = "professor_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private List<Course> courses;

  @MapKey(name = "brand")
  @OneToMany(mappedBy = "professor")
  private Map<String, Car> cars;
}
