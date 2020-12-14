package com.ecole.school.web.POJO;

import com.ecole.school.models.Horaire;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Specialite;

import lombok.Data;

@Data
public class ChangeClasse {
    private Niveau niveau;
    private Specialite specialite;
    private Horaire horaire;
    private SousClasse sousClasse;
    private Inscription inscription;
    private boolean regulariser;
}
