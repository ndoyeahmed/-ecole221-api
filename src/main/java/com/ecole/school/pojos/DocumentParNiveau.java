package com.ecole.school.pojos;

import lombok.Data;

@Data
public class DocumentParNiveau {
    private Long id;
    private DocumentPOJO document;
    private Niveau niveau;
}
