package com.ecole.school.web.POJO;

import com.ecole.school.models.DocumentParEtudiant;
import com.ecole.school.models.Etudiant;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Utilisateur;

import java.util.*;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InscriptionPOJO {
    private Etudiant etudiant;
    private Inscription inscription;
    private Utilisateur pere;
    private Utilisateur mere;
    private Utilisateur tuteur;
    private SousClasse sousClasse;
    private List<DocumentParEtudiant> documents;
}
