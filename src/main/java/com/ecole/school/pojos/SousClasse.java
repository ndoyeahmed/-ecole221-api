package com.ecole.school.pojos;

import lombok.Data;

@Data
public class SousClasse {
    private Long id;
    private String libelle;
    private boolean etat;
    private Niveau niveau;
    private Specialite specialite;
}
