package com.ecole.school.pojos;

import lombok.Data;

@Data
public class ClasseSousClasse {
    private Long id;
    private Classe classe;
    private SousClasse sousClasse;
}
