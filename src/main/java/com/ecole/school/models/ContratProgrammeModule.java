package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString
public class ContratProgrammeModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int horaire;

    @ManyToOne
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    private Contrat contrat;
    @ManyToOne
    @JoinColumn(name = "programmeModule", referencedColumnName = "id")
    private ProgrammeModule programmeModule;
}
