package com.ecole.school.web.controllers.notes;

import com.ecole.school.models.*;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.notes.NotesService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.parametrages.ParametrageSpecialiteService;
import com.ecole.school.web.POJO.BulletinInscription;
import com.ecole.school.web.POJO.BulletinRecap;
import com.ecole.school.web.POJO.RecapNoteProgrammeModuleByProgrammeUE;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.InternalServerErrorException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<?> getAllNoteProgrammeModuleByInscription(@PathVariable Long inscriptionId) {
        try {
            List<NoteProgrammeModule> noteProgrammeModules = getAllNoteProgrammeModuleByInscriptionId(inscriptionId);

            return ResponseEntity.ok(noteProgrammeModules);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal Server error");
        }
    }

    private List<NoteProgrammeModule> getAllNoteProgrammeModuleByInscriptionId(Long inscriptionId) {
        if (inscriptionId == null) throw new BadRequestException("inscriptionId required");

        Inscription inscription = inscriptionService.findInscriptionById(inscriptionId);
        if (inscription == null) throw new BadRequestException("inscription not found");

        List<ClasseSousClasse> classeSousClasseList = parametrageClasseService.findAllClasseSousClasseBySousClasse(inscription.getSousClasse());
        if (classeSousClasseList.size() <= 0) throw new BadRequestException("classesousclasse not found");
        Classe classe = classeSousClasseList.get(0).getClasse();
        SemestreNiveau semestreNiveau = parametrageSpecialiteService.findSemestreNiveauEncoursByNiveau(inscription.getSousClasse().getNiveau());
        if (semestreNiveau == null) throw new BadRequestException("semestre note found");
        Semestre semestre = semestreNiveau.getSemestre();
        AnneeScolaire anneeScolaire = inscription.getAnneeScolaire();
        ClasseReferentiel classeReferentiel = parametrageClasseService.findClasseReferentielByClasseAndAnneeScolaire(classe, anneeScolaire);
        if (classeReferentiel == null) throw new BadRequestException("classe not affected to a referentiel");

        return notesService.findAllNoteProgrammeModuleByInsAnneeRefSemestre(
          inscription, anneeScolaire, classeReferentiel.getReferentiel(), semestre);
    }

    @GetMapping("note-programme-module/inscription/{inscriptionId}/classe/{classeId}/semestre/{semestreId}")
    public ResponseEntity<?> getAllNoteProgrammeModuleByInsAnneeRefSemestre(@PathVariable Long inscriptionId,
                                                                           @PathVariable Long classeId, @PathVariable Long semestreId) {
        try {
            if (inscriptionId == null) throw new BadRequestException("inscriptionId required");
            if (classeId == null) throw new BadRequestException("classeId required");
            if (semestreId == null) throw new BadRequestException("semestreId required");

            Inscription inscription = inscriptionService.findInscriptionById(inscriptionId);
            if (inscription == null) throw new BadRequestException("inscription not found");
            Classe classe = parametrageClasseService.findClasseById(classeId);
            if (classe == null) throw new BadRequestException("classe not found");

            Semestre semestre = parametrageSpecialiteService.findSemestreById(semestreId);
            if (semestre == null) throw new BadRequestException("semestre not found");

            AnneeScolaire anneeScolaire = inscription.getAnneeScolaire();
            ClasseReferentiel classeReferentiel = parametrageClasseService.findClasseReferentielByClasseAndAnneeScolaire(classe, anneeScolaire);
            if (classeReferentiel == null) throw new BadRequestException("classe not affected to a referentiel");


            List<NoteProgrammeModule> noteProgrammeModules = notesService.findAllNoteProgrammeModuleByInsAnneeRefSemestre(
              inscription, anneeScolaire, classeReferentiel.getReferentiel(), semestre);

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

    // ------------------------------- bulletin etudiant ---------------------------------------------------------

    @GetMapping("bulletin/classe/{idClasse}")
    public ResponseEntity<?> getAllInfosBulletinByClasse(@PathVariable Long idClasse) {
        if (idClasse == null) throw new BadRequestException("idInscription required");

        Classe classe = parametrageClasseService.findClasseById(idClasse);
        if (classe == null) throw new BadRequestException("incorrect idClasse");

        return ResponseEntity.ok("sd");
    }

    @GetMapping("bulletin/inscription/{idInscription}")
    public ResponseEntity<?> getAllInfosBulletinByInscription(@PathVariable Long idInscription) {

        try {
            if (idInscription == null) throw new BadRequestException("idInscription required");
            Inscription inscription = inscriptionService.findInscriptionById(idInscription);
            if (inscription == null) throw new BadRequestException("idInscription incorrect");
            List<NoteProgrammeModule> noteProgrammeModules = getAllNoteProgrammeModuleByInscriptionId(idInscription);
            List<RecapNoteProgrammeModuleByProgrammeUE> recapNoteProgrammeModuleByProgrammeUES = groupeNoteProgrammeModuleByProgrammeUe(noteProgrammeModules);
            recapNoteProgrammeModuleByProgrammeUES.forEach(recapNoteProgrammeModuleByProgrammeUE -> {
                recapNoteProgrammeModuleByProgrammeUE = notesService.getMoyenneUEByRecapNoteProgrammeModule(recapNoteProgrammeModuleByProgrammeUE);
                notesService.checkAndSetValideProgrammeUE(inscription, recapNoteProgrammeModuleByProgrammeUE.getProgrammeUE(),
                  recapNoteProgrammeModuleByProgrammeUE.getMoyenneUE());
            });
            Double sommeMoyenneUE = recapNoteProgrammeModuleByProgrammeUES.stream().mapToDouble(recap -> recap.getMoyenneUE()*recap.getProgrammeUE().getCredit()).sum();
            Integer sommeCreditUE = recapNoteProgrammeModuleByProgrammeUES.stream().mapToInt(recap -> recap.getProgrammeUE().getCredit()).sum();
            Double moyenneGeneral = sommeMoyenneUE / sommeCreditUE;
            moyenneGeneral = notesService.formateFloatNumber(moyenneGeneral);
            BulletinInscription bulletinInscription = new BulletinInscription(recapNoteProgrammeModuleByProgrammeUES, moyenneGeneral);
            return ResponseEntity.ok(bulletinInscription);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping("bulletin-recap/inscription/{idInscription}")
    public ResponseEntity<?> getRecapBulletinByInscription(@PathVariable Long idInscription) {

        try {
            if (idInscription == null) throw new BadRequestException("idInscription required");
            Inscription inscription = inscriptionService.findInscriptionById(idInscription);
            if (inscription == null) throw new BadRequestException("idInscription incorrect");
            List<Semestre> semestres = parametrageSpecialiteService.findAllSemestre();
            List<BulletinRecap> bulletinRecaps = new ArrayList<>();
            semestres.forEach(semestre -> {
                BulletinRecap bulletinRecap = new BulletinRecap();
                bulletinRecap.setSemestre(semestre);
                Integer totalCreditSemestre = notesService.getSommeCreditProgrammeUeBySemestre(semestre, inscription);
                Integer totalCreditSemestreValide = notesService.getSommeCreditProgrammeUeNonValideBySemestre(semestre, inscription);
                bulletinRecap.setValide(totalCreditSemestreValide != null && totalCreditSemestre!= null && totalCreditSemestreValide >= totalCreditSemestre);
                bulletinRecap.setTotalCredit(totalCreditSemestreValide != null ? totalCreditSemestreValide : 0);
                bulletinRecaps.add(bulletinRecap);
            });
            return ResponseEntity.ok(bulletinRecaps);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping("bulletin/programmeue-inscription/inscription/{idInscription}")
    public ResponseEntity<?> getAllProgrammeUeInscriptionByInscription(@PathVariable Long idInscription) {

        try {
            if (idInscription == null) throw new BadRequestException("idInscription required");
            Inscription inscription = inscriptionService.findInscriptionById(idInscription);
            if (inscription == null) throw new BadRequestException("idInscription incorrect");

            return ResponseEntity.ok(notesService.findAllProgrammeUEInscriptionByInscription(inscription));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping("bulletin/recap-semestre-inscription/semestre/{idSemestre}/inscription/{idInscription}")
    public ResponseEntity<?> getAllRecapSemestreInscriptionValide(@PathVariable Long idSemestre, @PathVariable Long idInscription) {

        try {
            if (idInscription == null) throw new BadRequestException("idInscription required");
            if (idSemestre == null) throw new BadRequestException("idSemestre required");
            Inscription inscription = inscriptionService.findInscriptionById(idInscription);
            if (inscription == null) throw new BadRequestException("idInscription incorrect");

            Semestre semestre = parametrageSpecialiteService.findSemestreById(idSemestre);
            if (semestre == null) throw new BadRequestException("idSemestre incorrect");

            return ResponseEntity.ok(notesService.findAllProgrammeUEInscriptionByInscription(inscription));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    private List<RecapNoteProgrammeModuleByProgrammeUE> groupeNoteProgrammeModuleByProgrammeUe(List<NoteProgrammeModule> noteProgrammeModules) {
        List<RecapNoteProgrammeModuleByProgrammeUE> recapNoteProgrammeModuleByProgrammeUES = new ArrayList<>();
        List<ProgrammeUE> programmeUES = new ArrayList<>();

        for (int i = 0; i < noteProgrammeModules.size(); i++) {
            boolean trouve = false;
            for (int j = 0; j < i; j++) {
                if (Objects.equals(noteProgrammeModules.get(i).getProgrammeModule().getProgrammeUE().getId(),
                  noteProgrammeModules.get(j).getProgrammeModule().getProgrammeUE().getId())) {
                    trouve = true;
                    break;
                }
            }

            if (!trouve) {
                programmeUES.add(noteProgrammeModules.get(i).getProgrammeModule().getProgrammeUE());
            }
        }

        programmeUES.forEach(programmeUE -> {
            List<NoteProgrammeModule> programmeModules = new ArrayList<>();
            noteProgrammeModules.forEach(noteProgrammeModule -> {
                if (Objects.equals(noteProgrammeModule.getProgrammeModule().getProgrammeUE().getId(), programmeUE.getId())) {
                    programmeModules.add(noteProgrammeModule);
                }
            });
            RecapNoteProgrammeModuleByProgrammeUE recapNoteProgrammeModuleByProgrammeUE = new RecapNoteProgrammeModuleByProgrammeUE();
            recapNoteProgrammeModuleByProgrammeUE.setNoteProgrammeModules(programmeModules);
            recapNoteProgrammeModuleByProgrammeUE.setProgrammeUE(programmeUE);
            recapNoteProgrammeModuleByProgrammeUES.add(recapNoteProgrammeModuleByProgrammeUE);
        });

        return recapNoteProgrammeModuleByProgrammeUES;
    }

}
