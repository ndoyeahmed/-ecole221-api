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
public class ProgrammeModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String num;
    private int budget;
    private int coef;
    private int nbreCreditModule;
    private String syllabus;
    private int td; // nbr heures travaux dirigés
    private int tp; // nbr heures travaux pratique
    private int tpe; // nbr heures travail personnel etudiant
    private int vhp; // volume horaire presentiel
    private int cm; // nbr heures cours magistral
    private int vht; // volume heures total
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    private Module module;
    @ManyToOne
    @JoinColumn(name = "programmeUE", referencedColumnName = "id")
    private ProgrammeUE programmeUE;
}
