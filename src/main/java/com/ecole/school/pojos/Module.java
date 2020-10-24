package com.ecole.school.pojos;

import lombok.Data;

@Data
public class Module {
    private Long id;
    private String code;
    private String libelle;
    private boolean etat;
}
