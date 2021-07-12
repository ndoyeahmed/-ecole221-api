package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@ToString
public class EmargementProfesseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private boolean etat;

    @ManyToOne
    @JoinColumn(name = "planificationCours", referencedColumnName = "id")
    private PlanificationCours planificationCours;
}
