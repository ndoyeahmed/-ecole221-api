package com.ecole.school.pojos;

import lombok.Data;

@Data
public class DocumentParNiveau {
    private Long id;
    private Document document;
    private Niveau niveau;
}
