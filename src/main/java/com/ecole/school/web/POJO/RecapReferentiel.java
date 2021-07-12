package com.ecole.school.web.POJO;

import com.ecole.school.models.Semestre;
import lombok.Data;

import java.util.List;

@Data
public class RecapReferentiel {
    private Semestre semestre;
    private List<RecapProgrammeModule> listRecapProgrammeModule;
}
