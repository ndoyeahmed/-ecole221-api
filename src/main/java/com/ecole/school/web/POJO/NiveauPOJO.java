package com.ecole.school.web.POJO;

import java.util.List;

import com.ecole.school.models.DocumentParNiveau;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.SemestreNiveau;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NiveauPOJO {
    private Niveau niveau;
    private List<DocumentParNiveau> documentParNiveaus;
    private List<SemestreNiveau> semestreNiveaus;
}
