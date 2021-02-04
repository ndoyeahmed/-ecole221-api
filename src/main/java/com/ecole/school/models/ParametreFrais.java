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
public class ParametreFrais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int montant;

    @ManyToOne
    @JoinColumn(name = "frais", referencedColumnName = "id")
    private Frais frais;

    @ManyToOne
    @JoinColumn(name = "parametrePaiement", referencedColumnName = "id")
    private ParametrePaiement parametrePaiement;
}
