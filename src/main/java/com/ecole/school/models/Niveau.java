package com.ecole.school.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private int niveau;
    private boolean etat;

    @ManyToOne
    @JoinColumn(name = "cycle", referencedColumnName = "id")
    private Cycle cycle;
    @ManyToOne
    @JoinColumn(name = "parcours", referencedColumnName = "id")
    private Parcours parcours;
    @ManyToOne
    @JoinColumn(name = "semestre", referencedColumnName = "id")
    private Semestre semestre;
}
