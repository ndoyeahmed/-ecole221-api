package com.ecole.school.web.POJO;

import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import lombok.Data;

import java.util.List;

@Data
public class RecapProgrammeModule {
    private ProgrammeUE programmeUE;
    private List<ProgrammeModule> programmeModuleList;
}
