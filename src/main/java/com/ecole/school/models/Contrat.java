package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDebut;
    private Date dateFin;
    private boolean enCours;
    private int horaireVacataire;
    private int nombreHeure;
    private int montantContractuel;
    private String document;

    @ManyToOne
    @JoinColumn(name = "typeContrat", referencedColumnName = "id")
    private TypeContrat typeContrat;
    @ManyToOne
    @JoinColumn(name = "professeur", referencedColumnName = "id")
    private Professeur professeur;
}
