package com.ecole.school.web.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BulletinInscription {
  private List<RecapNoteProgrammeModuleByProgrammeUE> recapNoteProgrammeModuleByProgrammeUES;
  private Double moyenneGeneral;
}
