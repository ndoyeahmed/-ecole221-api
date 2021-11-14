package com.ecole.school.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class NoteProgrammeModule implements Comparable<NoteProgrammeModule> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note", referencedColumnName = "id")
    private Note note;
    @ManyToOne
    @JoinColumn(name = "programmeModule", referencedColumnName = "id")
    private ProgrammeModule programmeModule;

    @Override
    public int compareTo(NoteProgrammeModule o) {
        int compareNum = Integer.parseInt(o.getProgrammeModule().getNum());

        return Integer.parseInt(this.programmeModule.getNum()) - compareNum;
    }
}
