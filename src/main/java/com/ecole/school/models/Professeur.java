package com.ecole.school.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString
public class Professeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private int anneExperience;
    private String cv;
    private Date dateNaissance;
    private String diplome;
    private String email;
    private String grade;
    private String siteWeb;
    private String specialite;
    @Column(columnDefinition = "boolean default false")
    private boolean archive;
}
