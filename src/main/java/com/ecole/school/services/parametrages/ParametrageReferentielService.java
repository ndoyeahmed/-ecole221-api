package com.ecole.school.services.parametrages;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.Module;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.Specialite;
import com.ecole.school.repositories.ProgrammeModuleRepository;
import com.ecole.school.repositories.ProgrammeUERepository;
import com.ecole.school.repositories.ReferentielRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageReferentielService {
    private ReferentielRepository referentielRepository;
    private ProgrammeModuleRepository programmeModuleRepository;
    private ProgrammeUERepository programmeUERepository;

    @Autowired
    public ParametrageReferentielService(ReferentielRepository referentielRepository,
            ProgrammeModuleRepository programmeModuleRepository, ProgrammeUERepository programmeUERepository) {
        this.referentielRepository = referentielRepository;
        this.programmeModuleRepository = programmeModuleRepository;
        this.programmeUERepository = programmeUERepository;
    }

    // ----------------- REFERENTIEL SERVICES
    public Referentiel addReferentiel(Referentiel referentiel) {
        try {
            referentielRepository.save(referentiel);
            return referentiel;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Referentiel findReferentielById(Long id) {
        return referentielRepository.findById(id).orElse(null);
    }

    public List<Referentiel> findAllReferentiel() {
        return referentielRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<Referentiel> findAllReferentielByNiveau(Niveau niveau) {
        return referentielRepository.findAllByNiveauAndArchiveFalse(niveau).orElse(new ArrayList<>());
    }

    public List<Referentiel> findAllReferentielBySpecialite(Specialite specialite) {
        return referentielRepository.findAllBySpecialiteAndArchiveFalse(specialite).orElse(new ArrayList<>());
    }

    public List<Referentiel> findAllReferentielByNiveauAndSpecialite(Niveau niveau, Specialite specialite) {
        return referentielRepository.findAllByNiveauAndSpecialiteAndArchiveFalse(niveau, specialite).orElse(new ArrayList<>());
    }
    
    public Referentiel findReferentielByNiveauAndSpecialiteAndAnnee(Niveau niveau, Specialite specialite, int annee) {
        return referentielRepository.findByNiveauAndSpecialiteAndAnneeAndArchiveFalse(niveau, specialite, annee).orElse(null);
    }

    // ----------------- PROGRAMME_UE SERVICES
    public ProgrammeUE addProgrammeUE(ProgrammeUE programmeUE) {
        try {
            programmeUERepository.save(programmeUE);
            return programmeUE;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public ProgrammeUE findProgrammeUEById(Long id) {
        return programmeUERepository.findById(id).orElse(null);
    }

    public List<ProgrammeUE> findAllProgrammeUE() {
        return programmeUERepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<ProgrammeUE> findAllProgrammeUEByReferentiel(Referentiel referentiel) {
        return programmeUERepository.findAllByReferentielAndArchiveFalse(referentiel).orElse(new ArrayList<>());
    }

    public List<ProgrammeUE> findAllProgrammeUEBySemestre(Semestre semestre) {
        return programmeUERepository.findAllBySemestreAndArchiveFalse(semestre).orElse(new ArrayList<>());
    }

    public List<ProgrammeUE> findAllProgrammeUEByReferentielAndSemestre(Referentiel referentiel, Semestre semestre) {
        return programmeUERepository.findAllByReferentielAndSemestreAndArchiveFalse(referentiel, semestre).orElse(new ArrayList<>());
    }

    // ----------------- PROGRAMME_MODULE SERVICES
    public ProgrammeModule addProgrammeModule(ProgrammeModule programmeModule) {
        try {
            programmeModuleRepository.save(programmeModule);
            return programmeModule;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public ProgrammeModule finProgrammeModuleById(Long id) {
        return programmeModuleRepository.findById(id).orElse(null);
    }

    public List<ProgrammeModule> findAllProgrammeModule() {
        return programmeModuleRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<ProgrammeModule> findAllProgrammeModuleByProgrammeUE(ProgrammeUE programmeUE) {
        return programmeModuleRepository.findAllByProgrammeUEAndArchiveFalse(programmeUE).orElse(new ArrayList<>());
    }

    public List<ProgrammeModule> findAllProgrammeModuleByModule(Module module) {
        return programmeModuleRepository.findAllByModuleAndArchiveFalse(module).orElse(new ArrayList<>());
    }

    public List<ProgrammeModule> findAllProgrammeModuleByModuleAndProgrammeUE(Module module, ProgrammeUE programmeUE) {
        return programmeModuleRepository.findAllByModuleAndProgrammeUEAndArchiveFalse(module, programmeUE).orElse(new ArrayList<>());
    }
}
