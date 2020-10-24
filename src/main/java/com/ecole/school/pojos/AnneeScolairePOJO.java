package com.ecole.school.pojos;

import lombok.Data;

@Data
public class AnneeScolairePOJO {
    private Long id;
    private String libelle;
    private boolean enCours;
    private boolean archive;
}
