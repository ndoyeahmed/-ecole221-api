package com.ecole.school.services.professeur;

import com.ecole.school.models.Contrat;
import com.ecole.school.models.Professeur;
import com.ecole.school.models.ProfesseurModule;
import com.ecole.school.models.TypeContrat;
import com.ecole.school.repositories.ContratRepository;
import com.ecole.school.repositories.ProfesseurModuleRepository;
import com.ecole.school.repositories.ProfesseurRepository;
import com.ecole.school.repositories.TypeContratRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log
public class ProfesseurService {

    private TypeContratRepository typeContratRepository;
    private ProfesseurRepository professeurRepository;
    private ProfesseurModuleRepository professeurModuleRepository;
    private ContratRepository contratRepository;

    @Autowired
    public ProfesseurService(TypeContratRepository typeContratRepository, ProfesseurRepository professeurRepository,
                             ProfesseurModuleRepository professeurModuleRepository, ContratRepository contratRepository) {
        this.typeContratRepository = typeContratRepository;
        this.professeurModuleRepository = professeurModuleRepository;
        this.professeurRepository = professeurRepository;
        this.contratRepository = contratRepository;
    }

    public Professeur addProfesseur(Professeur professeur) {
        try {
            professeurRepository.save(professeur);
            return professeur;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public Professeur findProfesseurById(Long id) {
        return professeurRepository.findById(id).orElse(null);
    }

    public ProfesseurModule addProfesseurModule(ProfesseurModule professeurModule) {
        try {
            professeurModuleRepository.save(professeurModule);
            return professeurModule;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public List<ProfesseurModule> addProfesseurModuleList(List<ProfesseurModule> professeurModules) {
        try {
            professeurModuleRepository.saveAll(professeurModules);
            return professeurModules;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public Contrat addContrat(Contrat contrat) {
        try {
            contratRepository.save(contrat);
            return contrat;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public Contrat findContratEncoursByProfesseur(Professeur professeur) {
        return contratRepository.findByEnCoursTrueAndProfesseur(professeur).orElse(null);
    }

    public TypeContrat addTypeContrat(TypeContrat typeContrat) {
        try {
            typeContratRepository.save(typeContrat);
            return typeContrat;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    public List<TypeContrat> findAllTypeContrat() {
        return typeContratRepository.findAll();
    }

    public List<Professeur> findAllProfesseur() {
        return professeurRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<ProfesseurModule> findAllProfesseurModuleByProfesseur(Professeur professeur) {
        return professeurModuleRepository.findAllByProfesseur(professeur).orElse(new ArrayList<>());
    }
}
