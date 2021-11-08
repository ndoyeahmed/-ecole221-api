package com.ecole.school.web.POJO;

import com.ecole.school.models.Semestre;
import lombok.Data;

@Data
public class BulletinRecap {
  private Semestre semestre;
  private boolean valide;
  private Integer totalCredit;
  private Integer totalCreditSemestre;
}
