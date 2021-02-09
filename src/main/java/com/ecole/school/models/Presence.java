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
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean etat;
    private String motif;

    @ManyToOne
    @JoinColumn(name = "inscription", referencedColumnName = "id")
    private Inscription inscription;

    @ManyToOne
    @JoinColumn(name = "Jour", referencedColumnName = "id")
    private Jour jour;
}
