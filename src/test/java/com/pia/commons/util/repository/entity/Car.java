package com.pia.commons.util.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abdullah Beker
 */
@Getter
@Setter
@Table
@Entity
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String brand;
  private String licensePlate;

  @JoinColumns({
    @JoinColumn(name = "professor_name", referencedColumnName = "name"),
    @JoinColumn(name = "professor_surname", referencedColumnName = "surname")
  })
  @ManyToOne(fetch = FetchType.LAZY)
  private Professor professor;
}
