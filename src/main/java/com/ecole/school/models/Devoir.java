package com.ecole.school.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Devoir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;
    private Timestamp dateSaisie;
    private Timestamp dateMAJ;
    private double noteDevoire;

    @ManyToOne
    @JoinColumn(name = "note", referencedColumnName = "id")
    private Note note;
}
