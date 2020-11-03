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
public class ProgrammeUE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private int credit;
    private int fondamental;
    private int nbreHeureUE;
    private int num;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "referentiel", referencedColumnName = "id")
    private Referentiel referentiel;
    @ManyToOne
    @JoinColumn(name = "semestre", referencedColumnName = "id")
    private Semestre semestre;
    @ManyToOne
    @JoinColumn(name = "ue", referencedColumnName = "id")
    private UE ue;
}
