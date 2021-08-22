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
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp dateSaisie;
    private Timestamp dateMAJ;
    // moyenne des devoirs
    private double mds;
    // note examen final
    private double nef;
    // 0 = session normale; 1 = session remplacement;
    private int session;

    @ManyToOne
    @JoinColumn(name = "inscription", referencedColumnName = "id")
    private Inscription inscription;
}
