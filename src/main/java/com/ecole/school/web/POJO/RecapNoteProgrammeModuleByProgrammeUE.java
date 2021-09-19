package com.ecole.school.web.POJO;

import com.ecole.school.models.NoteProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import lombok.Data;

import java.util.List;

@Data
public class RecapNoteProgrammeModuleByProgrammeUE {
  private ProgrammeUE programmeUE;
  private List<NoteProgrammeModule> noteProgrammeModules;
  private double moyenneUE;
}
