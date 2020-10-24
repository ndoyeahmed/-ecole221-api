package com.ecole.school.pojos;

import java.util.Date;

import lombok.Data;

@Data
public class Referentiel {
    private Long id;
    private String description;
    private int annee;
    private int credit;
    private int volumeHeureTotal;
    private Date date;
    private Niveau niveau;
    private Specialite specialite;
}
