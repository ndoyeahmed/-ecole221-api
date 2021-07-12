package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString
public class PlanificationCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer heureDebut;
    private Integer heureFin;
    private int horaire;
    private Date jour;
    private Date periodeDebut;
    private Date periodeFin;

    @ManyToOne
    @JoinColumn(name = "anneeScolaire", referencedColumnName = "id")
    private AnneeScolaire anneeScolaire;
    @ManyToOne
    @JoinColumn(name = "programmeModule", referencedColumnName = "id")
    private ProgrammeModule programmeModule;
}
