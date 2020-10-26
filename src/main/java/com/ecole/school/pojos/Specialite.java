package com.ecole.school.pojos;

import lombok.Data;

@Data
public class Specialite {
    private Long id;
    private String libelle;
    private String num;
    private boolean etat;
    private MentionPOJO mention;
}
