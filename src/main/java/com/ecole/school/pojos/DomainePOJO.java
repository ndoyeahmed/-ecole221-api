package com.ecole.school.pojos;

import lombok.Data;

@Data
public class DomainePOJO {
    private Long id;
    private String libelle;
    private boolean etat;
    private boolean archive;
}
