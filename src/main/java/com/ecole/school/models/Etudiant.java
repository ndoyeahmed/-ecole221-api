package com.ecole.school.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String matricule;
    private int abandon;
    private String nom;
    private String prenom;
    private String email;
    private String emailPro;
    private String telephone;
    private Date dateNaissance;
    private String lieuNaissance;
    private String genre;
    private String etablissementPrecedent;
    private String motifEntree;
    private String niveauEntree;
    private String metier;
    private String ambition;
    private String autresRenseignements;
    private String photo;
    @Transient
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "pays", referencedColumnName = "id")
    private Pays pays;
}
