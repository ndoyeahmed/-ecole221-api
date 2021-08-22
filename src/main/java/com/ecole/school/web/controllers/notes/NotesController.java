package com.ecole.school.web.controllers.notes;

import com.ecole.school.models.*;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.notes.NotesService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.parametrages.ParametrageSpecialiteService;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.InternalServerErrorException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Log
@RestController
@CrossOrigin
@RequestMapping("/api/notes/")
public class NotesController {

    private NotesService notesService;
    private ParametrageClasseService parametrageClasseService;
    private ParametrageBaseService parametrageBaseService;
    private ParametrageReferentielService parametrageReferentielService;
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private InscriptionService inscriptionService;

    @Autowired
    public NotesController(NotesService notesService, ParametrageClasseService parametrageClasseService, ParametrageSpecialiteService parametrageSpecialiteService,
                           ParametrageBaseService parametrageBaseService, ParametrageReferentielService parametrageReferentielService, InscriptionService inscriptionService) {
        this.notesService = notesService;
        this.parametrageClasseService = parametrageClasseService;
        this.parametrageBaseService = parametrageBaseService;
        this.parametrageReferentielService = parametrageReferentielService;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
        this.inscriptionService = inscriptionService;
    }

    // ---------------- note programme module --------------------
    @GetMapping("note-programme-module/sousclasse/{idSousClasse}")
    public ResponseEntity<?> getAllNoteProgrammeModule(@PathVariable Long idSousClasse) {
        try {
            if (idSousClasse == null) throw new BadRequestException("idSousClasse required");

            SousClasse sousClasse = parametrageClasseService.findSousClasseById(idSousClasse);
            if (sousClasse == null) throw new BadRequestException("sousclasse note found");

            List<NoteProgrammeModule> noteProgrammeModules = notesService.findAllNoteProgrammeModuleBySousClasse(sousClasse);

            return ResponseEntity.ok(noteProgrammeModules);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal Server Error");
        }
    }

    @GetMapping("note-programme-module/programme-module/{programmeModuleId}/inscription/anneescolaire/{anneescolaireId}")
    public ResponseEntity<?> getAllNoteProgrammeModuleByProgrammeModuleAndInscriptionAnneeScolaire(@PathVariable Long programmeModuleId, @PathVariable Long anneescolaireId) {
        if (anneescolaireId == null) throw new BadRequestException("anneescolaireId required");
        if (programmeModuleId == null) throw new BadRequestException("programmeModuleId required");

        ProgrammeModule programmeModule = parametrageReferentielService.finProgrammeModuleById(programmeModuleId);
        if (programmeModule == null) throw new BadRequestException("bad programme module id");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(anneescolaireId);
        if (anneeScolaire == null) throw new BadRequestException("bad anneescolaire id");

        return ResponseEntity.ok(notesService
                .findAllNoteProgrammeModuleByProgrammeModuleAndInscriptionAnneeScolaire(programmeModule, anneeScolaire));
    }

    @GetMapping("note-programme-module/inscription/{inscriptionId}")
    public ResponseEntity<?> getAllNoteProgrammeModuleByInsAnneeRefSemestre(@PathVariable Long inscriptionId) {
        try {
            if (inscriptionId == null) throw new BadRequestException("inscriptionId required");

            Inscription inscription = inscriptionService.findInscriptionById(inscriptionId);
            if (inscription == null) throw new BadRequestException("inscription not found");



            List<NoteProgrammeModule> noteProgrammeModules = notesService.findAllNoteProgrammeModuleByInscription(inscription);

            return ResponseEntity.ok(noteProgrammeModules);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal Server error");
        }
    }

    // ---------------------------- Devoirs ---------------------------------
    @GetMapping("devoirs/note/{noteId}")
    public ResponseEntity<?> getAllDevoirsByNote(@PathVariable Long noteId) {
        if (noteId == null) throw new BadRequestException("noteId required");

        Note note = notesService.findNoteById(noteId);
        if (note == null) throw new BadRequestException("note not found");

        return ResponseEntity.ok(notesService.findAllDevoirByNote(note));
    }

    @PostMapping("devoirs")
    public ResponseEntity<?> addDevoir(@RequestBody Devoir devoir) {
        if (devoir == null) throw new BadRequestException("devoir required");
        if (devoir.getNote() == null) throw new BadRequestException("note devoir required");

        devoir.setArchive(false);
        devoir.setDateSaisie(Timestamp.valueOf(LocalDateTime.now()));
        devoir.setDateMAJ(Timestamp.valueOf(LocalDateTime.now()));

        return ResponseEntity.status(HttpStatus.CREATED).body(notesService.addDevoir(devoir));
    }

    @PostMapping("devoirs-list")
    public ResponseEntity<?> addDevoirsList(@RequestBody List<Devoir> devoirs) {
        if (devoirs == null) throw new BadRequestException("devoirlist required");
        if (devoirs.size() <= 0) throw new BadRequestException("devoirlist required");

        return ResponseEntity.status(HttpStatus.CREATED).body(notesService.addListDevoirs(devoirs));
    }

    @PutMapping("devoirs/{devoirId}")
    public ResponseEntity<?> updateDevoir(@PathVariable Long devoirId, @RequestBody Devoir devoir) {
        if (devoirId == null) throw new BadRequestException("devoirId required");
        if (devoir == null) throw new BadRequestException("devoir required");
        if (devoir.getNote() == null) throw new BadRequestException("note devoir required");

        Devoir devoir1 = notesService.findDevoirById(devoirId);
        if (devoir1 == null) throw new BadRequestException("devoirId not exist");

        devoir1.setNoteDevoire(devoir.getNoteDevoire());
        return ResponseEntity.status(HttpStatus.CREATED).body(notesService.addDevoir(devoir1));
    }

    // --------------------- notes -------------------------------------
    @GetMapping("inscription/anneescolaire/{anneescolaireId}")
    public ResponseEntity<?> getAllNotesByInscriptionAnneeScolaire(@PathVariable Long anneescolaireId) {
        if (anneescolaireId == null) throw new BadRequestException("anneescolaireId required");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(anneescolaireId);

        if (anneeScolaire == null) throw new BadRequestException("bad id");

        return ResponseEntity.ok(notesService
                .findAllNoteByInscriptionAnneeScolaireAndInscriptionArchiveFalse(anneeScolaire));
    }

    @PutMapping("{noteId}")
    public ResponseEntity<?> updateMoyenneDevoir(@PathVariable Long noteId, @RequestBody Note note) {
        if (note == null) throw new BadRequestException("note object can not be null");
        if (noteId == null) throw new BadRequestException("noteId required");

        Note note1 = notesService.findNoteById(noteId);
        if (note1 == null) throw new BadRequestException("noteId incorrect");

        note1.setMds(note.getMds());

        return ResponseEntity.status(HttpStatus.CREATED).body(notesService.addNote(note1));
    }

    @PutMapping("programme-module/{programmeModuleId}")
    public ResponseEntity<?> updateNote(@PathVariable Long programmeModuleId, @RequestBody Note note) {
        if (programmeModuleId == null) throw new BadRequestException("programmeModuleId can not be null");
        if (note == null) throw new BadRequestException("note object can not be null");
        if (note.getId() == null) throw new BadRequestException("note id can not be null");

        ProgrammeModule programmeModule = parametrageReferentielService.finProgrammeModuleById(programmeModuleId);
        if (programmeModule == null) throw new BadRequestException("bad programmeModuleId");

        note.setDateMAJ(Timestamp.valueOf(LocalDateTime.now()));
        note = notesService.addNote(note);

        if (notesService.checkValidateProgrammeUe(programmeModule.getProgrammeUE(), note.getInscription())) {
            ProgrammeUEInscription programmeUEInscription = parametrageReferentielService
                    .findProgrammeUEInscriptionByProgrammeUEAndInscription(programmeModule.getProgrammeUE(), note.getInscription());

            if (programmeUEInscription != null) {
                programmeUEInscription.setValide(true);
                parametrageReferentielService.addProgrammeUEInscription(programmeUEInscription);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }
}
