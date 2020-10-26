package com.ecole.school.pojos;

import lombok.Data;

@Data
public class MentionUE {
    private Long id;
    private MentionPOJO mention;
    private UE ue;
}
