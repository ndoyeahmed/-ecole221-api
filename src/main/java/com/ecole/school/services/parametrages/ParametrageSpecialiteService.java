package com.ecole.school.services.parametrages;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.Cycle;
import com.ecole.school.models.DocumentParNiveau;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.NiveauSpecialite;
import com.ecole.school.models.Parcours;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.SemestreNiveau;
import com.ecole.school.models.Specialite;
import com.ecole.school.repositories.DocumentParNiveauRepository;
import com.ecole.school.repositories.NiveauRepository;
import com.ecole.school.repositories.NiveauSpecialiteRepository;
import com.ecole.school.repositories.SemestreNiveauRepository;
import com.ecole.school.repositories.SemestreRepository;
import com.ecole.school.repositories.SpecialiteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageSpecialiteService {
    private SemestreRepository semestreRepository;
    private NiveauRepository niveauRepository;
    private DocumentParNiveauRepository documentParNiveauRepository;
    private SemestreNiveauRepository semestreNiveauRepository;
    private NiveauSpecialiteRepository niveauSpecialiteRepository;
    private SpecialiteRepository specialiteRepository;

    @Autowired
    public ParametrageSpecialiteService(SemestreRepository semestreRepository, NiveauRepository niveauRepository,
            DocumentParNiveauRepository documentParNiveauRepository, SpecialiteRepository specialiteRepository,
            SemestreNiveauRepository semestreNiveauRepository, NiveauSpecialiteRepository niveauSpecialiteRepository) {
        this.semestreRepository = semestreRepository;
        this.niveauRepository = niveauRepository;
        this.documentParNiveauRepository = documentParNiveauRepository;
        this.semestreNiveauRepository = semestreNiveauRepository;
        this.niveauSpecialiteRepository = niveauSpecialiteRepository;
        this.specialiteRepository = specialiteRepository;
    }

    // ----------------- SEMESTRE SERVICES
    public Semestre addSemestre(Semestre semestre) {
        try {
            semestreRepository.save(semestre);
            return semestre;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Semestre findSemestreById(Long id) {
        return semestreRepository.findById(id).orElse(null);
    }

    public List<Semestre> findAllSemestre() {
        return semestreRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- NIVEAU SERVICES
    public Niveau addNiveau(Niveau niveau) {
        try {
            niveauRepository.save(niveau);
            return niveau;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Niveau findNiveauById(Long id) {
        return niveauRepository.findById(id).orElse(null);
    }

    public List<Niveau> findAllNiveau() {
        return niveauRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<Niveau> findAllNiveauByCycle(Cycle cycle) {
        return niveauRepository.findAllByCycle(cycle).orElse(new ArrayList<>());
    }

    public List<Niveau> findAllNiveauByParcours(Parcours parcours) {
        return niveauRepository.findAllByParcours(parcours).orElse(new ArrayList<>());
    }

    public List<Niveau> findAllNiveauBySemestre(Semestre semestre) {
        return niveauRepository.findAllBySemestre(semestre).orElse(new ArrayList<>());
    }

    // ----------------- DOCUMENT PAR NIVEAU SERVICES
    public DocumentParNiveau addDocumentParNiveau(DocumentParNiveau documentParNiveau) {
        try {
            documentParNiveauRepository.save(documentParNiveau);
            return documentParNiveau;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<DocumentParNiveau> findAllDocumentParNiveauByNiveau(Niveau niveau) {
        return documentParNiveauRepository.findAllByNiveauAndArchiveFalse(niveau).orElse(new ArrayList<>());
    }

    // ----------------- SEMESTRE PAR NIVEAU SERVICES
    public SemestreNiveau addSemestreNiveau(SemestreNiveau semestreNiveau) {
        try {
            semestreNiveauRepository.save(semestreNiveau);
            return semestreNiveau;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<SemestreNiveau> findAllSemestreNiveauByNiveau(Niveau niveau) {
        return semestreNiveauRepository.findAllByNiveauAndArchiveFalse(niveau).orElse(new ArrayList<>());
    }

    // ----------------- SPECIALITE SERVICES
    public Specialite addSpecialite(Specialite specialite) {
        try {
            specialiteRepository.save(specialite);
            return specialite;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Specialite findSpecialiteById(Long id) {
        return specialiteRepository.findById(id).orElse(null);
    }

    public List<Specialite> findAllSpecialite() {
        return specialiteRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- NIVEAU SPECIALITE SERVICES
    public NiveauSpecialite addNiveauSpecialite(NiveauSpecialite niveauSpecialite) {
        try {
            niveauSpecialiteRepository.save(niveauSpecialite);
            return niveauSpecialite;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<NiveauSpecialite> findAllNiveauSpecialiteBySpecialite(Specialite specialite) {
        return niveauSpecialiteRepository.findAllBySpecialiteAndArchiveFalse(specialite).orElse(new ArrayList<>());
    }
}
