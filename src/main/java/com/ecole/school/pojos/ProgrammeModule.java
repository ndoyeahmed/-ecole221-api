package com.ecole.school.pojos;

import lombok.Data;

@Data
public class ProgrammeModule {
    private Long id;
    private String code;
    private String num;
    private int budget;
    private int coef;
    private int nbreCreditModule;
    private int td;
    private int tp;
    private int tpe;
    private int vh;
    private int vht;
    private Module module;
    private ProgrammeUE programmeUE;
}
