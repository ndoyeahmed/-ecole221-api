package com.ecole.school.services.notes;

import com.ecole.school.models.*;
import com.ecole.school.repositories.*;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.web.POJO.RecapNoteProgrammeModuleByProgrammeUE;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log
public class NotesService {

    private NoteProgrammeModuleRepository noteProgrammeModuleRepository;
    private NoteRepository noteRepository;
    private DevoirRepository devoirRepository;
    private ProgrammeUEInscriptionRepository programmeUEInscriptionRepository;
    private RecapSemestreInscriptionValideRepository recapSemestreInscriptionValideRepository;
    private ParametrageReferentielService parametrageReferentielService;

    @Autowired
    public NotesService(NoteProgrammeModuleRepository noteProgrammeModuleRepository, DevoirRepository devoirRepository,
      RecapSemestreInscriptionValideRepository recapSemestreInscriptionValideRepository,
                        NoteRepository noteRepository, ParametrageReferentielService parametrageReferentielService, ProgrammeUEInscriptionRepository programmeUEInscriptionRepository) {
        this.noteProgrammeModuleRepository = noteProgrammeModuleRepository;
        this.noteRepository = noteRepository;
        this.parametrageReferentielService = parametrageReferentielService;
        this.devoirRepository = devoirRepository;
        this.programmeUEInscriptionRepository = programmeUEInscriptionRepository;
        this.recapSemestreInscriptionValideRepository = recapSemestreInscriptionValideRepository;
    }

    // ----------------------- Devoirs --------------------------------------

    public List<Devoir> findAllDevoirByNote(Note note) {
        return devoirRepository.findAllByNoteAndArchiveFalse(note).orElse(new ArrayList<>());
    }

    public Devoir addDevoir(Devoir devoir) {
        try {
            devoirRepository.save(devoir);
            return devoir;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public List<Devoir> addListDevoirs(List<Devoir> devoirs) {
        try {
            devoirRepository.saveAll(devoirs);
            return devoirs;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public Devoir findDevoirById(Long id) {
        return devoirRepository.findById(id).orElse(null);
    }

    // ---------------------- note programme modules ----------------------------------------

    public NoteProgrammeModule addNoteProgrammeModule(NoteProgrammeModule noteProgrammeModule) {
        try {
            noteProgrammeModuleRepository.save(noteProgrammeModule);
            return noteProgrammeModule;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public NoteProgrammeModule findNoteProgrammeModuleByProgrammeAndInscription(ProgrammeModule programmeModule, Inscription inscription) {
        return noteProgrammeModuleRepository.findByProgrammeModuleAndNote_Inscription(programmeModule, inscription).orElse(null);
    }

    public List<NoteProgrammeModule> addListNoteProgrammeModule(List<NoteProgrammeModule> noteProgrammeModules) {
        try {
            noteProgrammeModuleRepository.saveAll(noteProgrammeModules);
            return noteProgrammeModules;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public List<NoteProgrammeModule> findAllNoteProgrammeModuleBySousClasse(SousClasse sousClasse) {
        return this.noteProgrammeModuleRepository.findAllByNote_Inscription_SousClasse(sousClasse).orElse(new ArrayList<>());
    }

    public List<NoteProgrammeModule> findAllNoteProgrammeModuleByProgrammeModuleAndInscriptionAnneeScolaire(ProgrammeModule programmeModule, AnneeScolaire anneeScolaire) {
        return this.noteProgrammeModuleRepository.findAllByProgrammeModuleAndNote_Inscription_AnneeScolaire(programmeModule, anneeScolaire).orElse(new ArrayList<>());
    }

    /**
     * find all NoteProgrammeModule by Inscription & AnneeScolaire && Referentiel && Sesmestre
     * @return list NoteProgrammeModule
     */
    public List<NoteProgrammeModule> findAllNoteProgrammeModuleByInsAnneeRefSemestre(Inscription inscription, AnneeScolaire anneeScolaire, Referentiel referentiel, Semestre semestre) {
        return noteProgrammeModuleRepository.findAllByNote_InscriptionAndNote_Inscription_AnneeScolaireAndProgrammeModule_ProgrammeUE_ReferentielAndProgrammeModule_ProgrammeUE_Semestre
          (inscription, anneeScolaire, referentiel, semestre).orElse(new ArrayList<>());
    }

    public List<NoteProgrammeModule> findAllNoteProgrammeModuleByInscription(Inscription inscription) {
        return noteProgrammeModuleRepository.findAllByNote_Inscription(inscription).orElse(new ArrayList<>());
    }

    // ---------------------- note ----------------------------------------
    public Note addNote(Note note) {
        try {
            noteRepository.save(note);
            return note;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public List<Note> addListNote(List<Note> notes) {
        try {
            noteRepository.saveAll(notes);
            return notes;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public Note findNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public List<Note> findAllNoteByInscription(Inscription inscription) {
        return noteRepository.findAllByInscription(inscription).orElse(new ArrayList<>());
    }

    public List<Note> findAllNoteByInscriptionAnneeScolaireAndInscriptionArchiveFalse(AnneeScolaire anneeScolaire) {
        return noteRepository.findAllByInscription_AnneeScolaireAndInscription_ArchiveFalse(anneeScolaire).orElse(new ArrayList<>());
    }

    // --------------------------- programme ue inscription ---------------------------------
    public ProgrammeUEInscription addProgrammeUEInscription(ProgrammeUEInscription programmeUEInscription) {
        try {
            programmeUEInscriptionRepository.save(programmeUEInscription);
            return programmeUEInscription;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public ProgrammeUEInscription findProgrammeUEInscriptionByProgrammeUEAndInscription(ProgrammeUE programmeUE, Inscription inscription) {
        return programmeUEInscriptionRepository.findByProgrammeUEAndInscriptionAndArchiveFalse(programmeUE, inscription).orElse(null);
    }

    public List<ProgrammeUEInscription> findAllProgrammeUEInscriptionByInscription(Inscription inscription) {
        return programmeUEInscriptionRepository.findAllByArchiveFalseAndInscription(inscription).orElse(new ArrayList<>());
    }

    public Integer getSommeCreditProgrammeUeNonValideBySemestre(Semestre semestre, Inscription inscription) {
        return programmeUEInscriptionRepository.findSumCreditPrgrammeUeNonValideBySemestre(semestre, inscription);
    }

    public Integer getSommeCreditProgrammeUeBySemestre(Semestre semestre, Inscription inscription) {
        return programmeUEInscriptionRepository.findSumCreditPrgrammeUeBySemestre(semestre, inscription);
    }

    // ------------------------------ recap semestre inscription valide -----------------------------------------------
    public List<RecapSemestreInscriptionValide> findAllRecapSemestreInscriptionValideByInscription(Inscription inscription) {
        return recapSemestreInscriptionValideRepository.findAllByInscription(inscription).orElse(new ArrayList<>());
    }

    public RecapSemestreInscriptionValide addRecapSemestreInscriptionValide(RecapSemestreInscriptionValide recapSemestreInscriptionValide) {
        try {
            recapSemestreInscriptionValideRepository.save(recapSemestreInscriptionValide);
            return recapSemestreInscriptionValide;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public RecapSemestreInscriptionValide findRecapSemestreInscriptionValideById(Long id) {
        return recapSemestreInscriptionValideRepository.findById(id).orElse(null);
    }

    public RecapSemestreInscriptionValide findRecapSemestreInscriptionValideBySemestreAndInscription(Semestre semestre, Inscription inscription) {
        return recapSemestreInscriptionValideRepository.findBySemestreAndInscription(semestre, inscription).orElse(null);
    }

    public List<RecapSemestreInscriptionValide> findAllRecapSemestreInscriptionValideByInscriptionEtudiant(Etudiant etudiant) {
        return recapSemestreInscriptionValideRepository.findAllByInscription_Etudiant(etudiant).orElse(new ArrayList<>());
    }

    // ------------------ utils -----------------------------
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

    public List<NoteProgrammeModule> setDefaultNote(List<ProgrammeModule> programmeModules, Inscription inscription) {
        List<NoteProgrammeModule> noteProgrammeModules = new ArrayList<>();

            programmeModules.parallelStream().forEach(programmeModule -> {
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

        return noteProgrammeModules;
    }



    public ProgrammeUEInscription checkAndSetValideProgrammeUE(Inscription inscription, ProgrammeUE programmeUE, double moyenneUE) {
        ProgrammeUEInscription programmeUEInscription = findProgrammeUEInscriptionByProgrammeUEAndInscription(programmeUE, inscription);
        if (programmeUEInscription == null) {
            programmeUEInscription = new ProgrammeUEInscription();
            programmeUEInscription.setProgrammeUE(programmeUE);
            programmeUEInscription.setInscription(inscription);
            programmeUEInscription.setArchive(false);
        }

        programmeUEInscription.setValide(moyenneUE >= 10);

        addProgrammeUEInscription(programmeUEInscription);

        return programmeUEInscription;
    }



    public boolean checkValidateProgrammeUe(ProgrammeUE programmeUE, Inscription inscription) {
        List<ProgrammeModule> programmeModules = parametrageReferentielService.findAllProgrammeModuleByProgrammeUE(programmeUE.getId());
        if (programmeModules.size() > 0) {
            double sommeMoyenneModule = 0;
            for (ProgrammeModule pm : programmeModules) {
                NoteProgrammeModule noteProgrammeModule = findNoteProgrammeModuleByProgrammeAndInscription(pm, inscription);
                if (noteProgrammeModule != null) {
                    sommeMoyenneModule = sommeMoyenneModule + ((noteProgrammeModule.getNote().getMds() + noteProgrammeModule.getNote().getNef())/2);
                }
            }
            return sommeMoyenneModule / programmeModules.size() > 10;
        }
        return false;
    }

    public RecapNoteProgrammeModuleByProgrammeUE getMoyenneUEByRecapNoteProgrammeModule(RecapNoteProgrammeModuleByProgrammeUE recapNoteProgrammeModuleByProgrammeUE) {
      double sommeCoef = recapNoteProgrammeModuleByProgrammeUE.getNoteProgrammeModules().stream()
        .mapToDouble(noteProgrammeModule -> noteProgrammeModule.getProgrammeModule().getCoef()).sum();

      double sommeMoyenneProgrammeModule = recapNoteProgrammeModuleByProgrammeUE.getNoteProgrammeModules().stream().mapToDouble(
        noteProgrammeModule -> (getMoyenneProgrammeModule(noteProgrammeModule) *
                                noteProgrammeModule.getProgrammeModule().getCoef())).sum();
      double moyenneUe = sommeMoyenneProgrammeModule/sommeCoef;
        moyenneUe = formateFloatNumber(moyenneUe);
      recapNoteProgrammeModuleByProgrammeUE.setMoyenneUE(moyenneUe);

      return recapNoteProgrammeModuleByProgrammeUE;
    }

    public Double formateFloatNumber(Double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getMoyenneUE(ProgrammeUE programmeUE, Inscription inscription) {
        List<ProgrammeModule> programmeModules = parametrageReferentielService.findAllProgrammeModuleByProgrammeUE(programmeUE.getId());
        double moyenneUE = 0;
        if (programmeModules.size() > 0) {
            double sommeMoyenneModule = 0;
            double sommeCoef = 0;
            for (ProgrammeModule pm : programmeModules) {
                NoteProgrammeModule noteProgrammeModule = findNoteProgrammeModuleByProgrammeAndInscription(pm, inscription);
                if (noteProgrammeModule != null) {
                    sommeMoyenneModule = sommeMoyenneModule + (getMoyenneProgrammeModule(noteProgrammeModule) * noteProgrammeModule.getProgrammeModule().getCoef());
                    sommeCoef = sommeCoef + noteProgrammeModule.getProgrammeModule().getCoef();
                }
            }
            moyenneUE = sommeMoyenneModule / sommeCoef;
        }

        return moyenneUE;
    }

    public List<ProgrammeUEInscription> addListProgrammeUeInscriptionByReferentielAndInscription(Referentiel referentiel, Inscription inscription) {
        List<ProgrammeUEInscription> programmeUEInscriptions = new ArrayList<>();
        List<ProgrammeUE> programmeUES = parametrageReferentielService.findAllProgrammeUEByReferentiel(referentiel);
        if (programmeUES.size() > 0) {
            programmeUES.parallelStream().forEach(pu -> {
                ProgrammeUEInscription programmeUEInscription = new ProgrammeUEInscription();
                programmeUEInscription.setInscription(inscription);
                programmeUEInscription.setProgrammeUE(pu);
                programmeUEInscription.setArchive(false);
                programmeUEInscription.setValide(checkValidateProgrammeUe(pu, inscription));
                parametrageReferentielService.addProgrammeUEInscription(programmeUEInscription);
                programmeUEInscriptions.add(programmeUEInscription);
            });
        }
        log.info("programme ue inscription ------>   " + programmeUEInscriptions.size());
        return programmeUEInscriptions;
    }

    public Double getMoyenneProgrammeModule(NoteProgrammeModule noteProgrammeModule) {
        return (noteProgrammeModule.getNote().getMds() + noteProgrammeModule.getNote().getNef()) / 2;
    }
}
