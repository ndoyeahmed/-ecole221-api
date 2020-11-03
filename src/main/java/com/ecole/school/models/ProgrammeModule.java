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
    private int td;
    private int tp;
    private int tpe;
    private int vh;
    private int vht;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    private Module module;
    @ManyToOne
    @JoinColumn(name = "programmeUE", referencedColumnName = "id")
    private ProgrammeUE programmeUE;
}
