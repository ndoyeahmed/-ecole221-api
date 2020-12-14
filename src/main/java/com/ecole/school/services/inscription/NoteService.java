package com.ecole.school.services.inscription;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.Inscription;
import com.ecole.school.models.Note;
import com.ecole.school.models.NoteProgrammeModule;
import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.repositories.NoteProgrammeModuleRepository;
import com.ecole.school.repositories.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class NoteService {
    private NoteRepository noteRepository;
    private NoteProgrammeModuleRepository noteProgrammeModuleRepository;

    public NoteService(NoteRepository noteRepository, NoteProgrammeModuleRepository noteProgrammeModuleRepository) {
        this.noteRepository = noteRepository;
        this.noteProgrammeModuleRepository = noteProgrammeModuleRepository;
    }

    // NOTE SERVICE
    public Note addNote(Note note) {
        try {
            noteRepository.save(note);
            return note;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public NoteProgrammeModule addNoteProgrammeModule(NoteProgrammeModule noteProgrammeModule) {
        try {
            noteProgrammeModuleRepository.save(noteProgrammeModule);
            return noteProgrammeModule;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<NoteProgrammeModule> regulariseNote(List<List<ProgrammeModule>> pList, Inscription inscription) {
        List<NoteProgrammeModule> noteProgrammeModules = new ArrayList<>();
        pList.parallelStream().forEach(x -> {
            x.parallelStream().forEach(programmeModule -> {
                // set default note
                Note note = new Note();
                note.setDateSaisie(Timestamp.valueOf(LocalDateTime.now()));
                note.setInscription(inscription);
                note.setMds(0.0);
                note.setNef(0.0);
                note = addNote(note);

                // set affectation to note and programme module
                NoteProgrammeModule noteProgrammeModule = new NoteProgrammeModule();
                noteProgrammeModule.setNote(note);
                noteProgrammeModule.setProgrammeModule(programmeModule);
                noteProgrammeModule = addNoteProgrammeModule(noteProgrammeModule);
                noteProgrammeModules.add(noteProgrammeModule);
            });
        });

        return noteProgrammeModules;
    }
}
