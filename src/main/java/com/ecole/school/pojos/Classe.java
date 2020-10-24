package com.ecole.school.pojos;

import lombok.Data;

@Data
public class Classe {
    private Long id;
    private String libelle;
    private boolean etat;
    private Horaire horaire;
    private Niveau niveau;
    private Specialite specialite;
}
