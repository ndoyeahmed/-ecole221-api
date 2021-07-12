package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString
public class ProfesseurModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professeur", referencedColumnName = "id")
    private Professeur professeur;
    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    private Module module;
}
