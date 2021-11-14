package com.ecole.school.web.POJO;

import com.ecole.school.models.NoteProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import lombok.Data;

import java.util.List;

@Data
public class RecapNoteProgrammeModuleByProgrammeUE implements Comparable<RecapNoteProgrammeModuleByProgrammeUE> {
  private ProgrammeUE programmeUE;
  private List<NoteProgrammeModule> noteProgrammeModules;
  private double moyenneUE;
  private boolean valide;

  @Override
  public int compareTo(RecapNoteProgrammeModuleByProgrammeUE o) {
    int compareNum = o.getProgrammeUE().getNum();

    //ascending order
    return this.programmeUE.getNum() - compareNum;

    //descending order
    //return compareNum - this.num;
  }
}
