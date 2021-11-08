package com.ecole.school.services.inscription;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.DocumentParEtudiant;
import com.ecole.school.models.Etudiant;
import com.ecole.school.models.EtudiantTuteur;
import com.ecole.school.models.Inscription;
import com.ecole.school.repositories.DocumentParEtudiantRepository;
import com.ecole.school.repositories.EtudiantRepository;
import com.ecole.school.repositories.EtudiantTuteurRepository;
import com.ecole.school.repositories.InscriptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class InscriptionService {
    private EtudiantRepository etudiantRepository;
    private InscriptionRepository inscriptionRepository;
    private DocumentParEtudiantRepository documentParEtudiantRepository;
    private EtudiantTuteurRepository etudiantTuteurRepository;

    @Autowired
    public InscriptionService(EtudiantRepository etudiantRepository, InscriptionRepository inscriptionRepository,
        DocumentParEtudiantRepository documentParEtudiantRepository, EtudiantTuteurRepository etudiantTuteurRepository) {

        this.etudiantRepository = etudiantRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.documentParEtudiantRepository = documentParEtudiantRepository;
        this.etudiantTuteurRepository = etudiantTuteurRepository;
    }

    //----------- Etudiant service
    public Etudiant addEtudiant(Etudiant etudiant) {
        try {
            etudiantRepository.save(etudiant);
            return etudiant;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Etudiant findEtudiantById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    public Etudiant findEtudiantByCin(String cin) {
        return etudiantRepository.findByCin(cin).orElse(null);
    }

    public List<Etudiant> findAllEtudiant() {
        return etudiantRepository.findAll();
    }

    //----------- Inscription service
    public Inscription addInscription(Inscription inscription) {
        try {
            inscriptionRepository.save(inscription);
            return inscription;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Inscription findInscriptionById(Long id) {
        return inscriptionRepository.findById(id).orElse(null);
    }

    public Inscription findInscriptionByEtudiantAnneeSpecialiteNiveau(Long etudiantId, Long anneeId, Long specialiteId, Long niveauId) {
        return inscriptionRepository.findByEtudiantAndAnneeAndSpecialiteAndNiveau(etudiantId, anneeId, specialiteId, niveauId).orElse(null);
    }

    public List<Inscription> findAllInscriptionByAnneeScolaire(AnneeScolaire anneeScolaire) {
        return inscriptionRepository.findAllByAnneeScolaireAndArchiveFalse(anneeScolaire).orElse(new ArrayList<>());
    }

    public List<Inscription> findAllInscriptionByEtudiantId(Long idEtudiant) {
        return inscriptionRepository.findAllByEtudiantAndArchiveFalse(idEtudiant).orElse(new ArrayList<>());
    }

    public List<Inscription> findAllInscriptionByAnneeScolaireAndArchiveFalseAndHaveNote(AnneeScolaire anneeScolaire) {
        return inscriptionRepository.findAllByAnneeScolaireAndArchiveFalseAndHaveNote(anneeScolaire).orElse(new ArrayList<>());
    }

    //----------- DocumentParEtudiant service
    public DocumentParEtudiant addDocumentParEtudiant(DocumentParEtudiant documentParEtudiant) {
        try {
            documentParEtudiantRepository.save(documentParEtudiant);
            return documentParEtudiant;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    //----------- Etudiant tuteur service
    public EtudiantTuteur addEtudiantTuteur(EtudiantTuteur etudiantTuteur) {
        try {
            etudiantTuteurRepository.save(etudiantTuteur);
            return etudiantTuteur;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }
}
