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
public class DetailsParametreClasse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parametrePaiement", referencedColumnName = "id")
    private ParametrePaiement parametrePaiement;

    @ManyToOne
    @JoinColumn(name = "classe", referencedColumnName = "id")
    private Classe classe;
}
