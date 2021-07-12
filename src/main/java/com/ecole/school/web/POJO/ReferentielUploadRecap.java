package com.ecole.school.web.POJO;

import com.ecole.school.models.Referentiel;
import lombok.Data;

import java.util.List;

@Data
public class ReferentielUploadRecap {
    private Referentiel referentiel;
    private List<RecapReferentiel> recapReferentielList;
    private List<String> errorList;
}
