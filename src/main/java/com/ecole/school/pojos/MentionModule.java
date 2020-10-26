package com.ecole.school.pojos;

import lombok.Data;

@Data
public class MentionModule {
    private Long id;
    private MentionPOJO mention;
    private Module module;
}
