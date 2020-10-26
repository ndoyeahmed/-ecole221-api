package com.ecole.school.pojos;

import lombok.Data;

@Data
public class MentionPOJO {
    private Long id;
    private String libelle;
    private boolean etat;
    private DomainePOJO domaine;
    private boolean archive;
}
