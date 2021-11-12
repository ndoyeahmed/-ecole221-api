package com.ecole.school.models;

import com.ecole.school.models.Inscription;
import com.ecole.school.models.Semestre;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RecapSemestreInscriptionValide {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private boolean valide;
  private Integer nombreCreditSemestre;

  @ManyToOne
  @JoinColumn(name = "semestre", referencedColumnName = "id")
  private Semestre semestre;
  @ManyToOne
  @JoinColumn(name = "inscription", referencedColumnName = "id")
  private Inscription inscription;
}
