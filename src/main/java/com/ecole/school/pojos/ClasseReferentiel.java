package com.ecole.school.pojos;

import lombok.Data;

@Data
public class ClasseReferentiel {
    private Long id;
    private int anneeDebut;
    private int anneeFin;
    private AnneeScolairePOJO anneeScolaire;
    private Classe classe;
    private Referentiel referentiel;
}
