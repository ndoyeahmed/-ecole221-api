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
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int num;

    @ManyToOne
    @JoinColumn(name = "referentiel", referencedColumnName = "id")
    private Referentiel referentiel;
    @ManyToOne
    @JoinColumn(name = "anneeScolaire", referencedColumnName = "id")
    private AnneeScolaire anneeScolaire;
}
