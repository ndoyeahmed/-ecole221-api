package com.ecole.school.web.POJO;

import java.util.List;

import com.ecole.school.models.NiveauSpecialite;
import com.ecole.school.models.Specialite;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpecialitePOJO {
    private Specialite specialite;
    private List<NiveauSpecialite> niveauSpecialites;
}
