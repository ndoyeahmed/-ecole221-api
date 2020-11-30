package com.ecole.school.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class SousClasse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private int nbrEleve;
    private boolean etat;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "horaire", referencedColumnName = "id")
    private Horaire horaire;
    @ManyToOne
    @JoinColumn(name = "niveau", referencedColumnName = "id")
    private Niveau niveau;
    @ManyToOne
    @JoinColumn(name = "specialite", referencedColumnName = "id")
    private Specialite specialite;
}
