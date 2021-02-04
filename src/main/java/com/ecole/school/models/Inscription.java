package com.ecole.school.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Entity
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private int passe;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "promotion", referencedColumnName = "id")
    private Promotion promotion;
    @ManyToOne
    @JoinColumn(name = "sousClasse", referencedColumnName = "id")
    private SousClasse sousClasse;
    @ManyToOne
    @JoinColumn(name = "anneeScolaire", referencedColumnName = "id")
    private AnneeScolaire anneeScolaire;
    @ManyToOne
    @JoinColumn(name = "etudiant", referencedColumnName = "id")
    private Etudiant etudiant;
    @ManyToOne
    @JoinColumn(name = "bourse", referencedColumnName = "id")
    private Bourse bourse;
}
