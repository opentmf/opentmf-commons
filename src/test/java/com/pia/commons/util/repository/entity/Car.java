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

  @JoinColumn(name = "professor_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Professor professor;
}
