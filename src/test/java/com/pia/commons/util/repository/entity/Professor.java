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

  @EmbeddedId private ProfessorId id;

  private OffsetDateTime createdOn;

  @OneToMany(mappedBy = "professor")
  private List<Student> student;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Classroom classroom;

  @ManyToMany
  @JoinTable(
      name = "professor_course",
      joinColumns = {
        @JoinColumn(name = "professor_name", referencedColumnName = "name"),
        @JoinColumn(name = "professor_surname", referencedColumnName = "surname")
      },
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private List<Course> courses;

  @MapKey(name = "brand")
  @OneToMany(mappedBy = "professor")
  private Map<String, Car> cars;
}
