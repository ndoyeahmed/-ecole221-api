package com.ecole.school.pojos;

import lombok.Data;

@Data
public class ProgrammeUE {
    private Long id;
    private String code;
    private int credit;
    private int fondamental;
    private int nbreHeureUE;
    private int num;
    private Referentiel referentiel;
    private SemestrePOJO semestre;
    private UE ue;
}
