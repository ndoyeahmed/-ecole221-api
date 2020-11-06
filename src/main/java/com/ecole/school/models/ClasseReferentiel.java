package com.ecole.school.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class ClasseReferentiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int anneeDebut;
    private int anneeFin;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;
    
    @ManyToOne
    @JoinColumn(name = "anneeScolaire", referencedColumnName = "id")
    private AnneeScolaire anneeScolaire;
    @ManyToOne
    @JoinColumn(name = "classe", referencedColumnName = "id")
    private Classe classe;
    @ManyToOne
    @JoinColumn(name = "referentiel", referencedColumnName = "id")
    private Referentiel referentiel;
}
