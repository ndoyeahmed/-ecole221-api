package com.ecole.school.pojos;

import lombok.Data;

@Data
public class Mention {
    private Long id;
    private String libelle;
    private boolean etat;
    private Domaine domaine;
}
