package com.ecole.school.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProgrammeUEInscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "boolean default false")
    private boolean valide;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "programmeUE", referencedColumnName = "id")
    private ProgrammeUE programmeUE;
    @ManyToOne
    @JoinColumn(name = "inscription", referencedColumnName = "id")
    private Inscription inscription;
}
