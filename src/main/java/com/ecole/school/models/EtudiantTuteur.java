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
public class EtudiantTuteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant", referencedColumnName = "id")
    private Etudiant etudiant;
    @ManyToOne
    @JoinColumn(name = "tuteur", referencedColumnName = "id")
    private Utilisateur tuteur;
}
