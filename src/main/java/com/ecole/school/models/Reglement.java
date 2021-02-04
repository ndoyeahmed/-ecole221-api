package com.ecole.school.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Reglement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private int montant;
    private int montantRestant;

    @ManyToOne
    @JoinColumn(name = "mois", referencedColumnName = "id")
    private Mois mois;
    @ManyToOne
    @JoinColumn(name = "inscription", referencedColumnName = "id")
    private Inscription inscription;
    @ManyToOne
    @JoinColumn(name = "typeReglement", referencedColumnName = "id")
    private TypeReglement typeReglement;
}
