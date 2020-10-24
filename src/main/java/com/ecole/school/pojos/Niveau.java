package com.ecole.school.pojos;

import lombok.Data;

@Data
public class Niveau {
    private Long id;
    private String libelle;
    private int niveau;
    private boolean etat;
    private Cycle cycle;
    private Parcours parcours;
    private Semestre semestre;
}
