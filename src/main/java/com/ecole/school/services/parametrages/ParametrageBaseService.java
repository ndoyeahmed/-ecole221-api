package com.ecole.school.services.parametrages;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.*;
import com.ecole.school.repositories.AnneeScolaireRepository;
import com.ecole.school.repositories.CycleRepository;
import com.ecole.school.repositories.DocumentRepository;
import com.ecole.school.repositories.DomaineRepository;
import com.ecole.school.repositories.HoraireRepository;
import com.ecole.school.repositories.MentionRepository;
import com.ecole.school.repositories.ParcoursRepository;
import com.ecole.school.repositories.PaysRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageBaseService {
    private AnneeScolaireRepository anneeScolaireRepository;
    private DomaineRepository domaineRepository;
    private CycleRepository cycleRepository;
    private ParcoursRepository parcoursRepository;
    private HoraireRepository horaireRepository;
    private DocumentRepository documentRepository;
    private MentionRepository mentionRepository;
    private PaysRepository paysRepository;

    @Autowired
    public ParametrageBaseService(AnneeScolaireRepository anneeScolaireRepository, CycleRepository cycleRepository,
                                DomaineRepository domaineRepository, ParcoursRepository parcoursRepository,
                                HoraireRepository horaireRepository, DocumentRepository documentRepository,
                                MentionRepository mentionRepository, PaysRepository paysRepository) {
        this.anneeScolaireRepository = anneeScolaireRepository;
        this.domaineRepository = domaineRepository;
        this.cycleRepository = cycleRepository;
        this.parcoursRepository = parcoursRepository;
        this.horaireRepository = horaireRepository;
        this.documentRepository = documentRepository;
        this.mentionRepository = mentionRepository;
        this.paysRepository = paysRepository;
    }

    // ----------------- ANNEE SCOLAIRE SERVICES
    public AnneeScolaire addAnneeScolaire(AnneeScolaire anneeScolaire) {
        try {
            anneeScolaireRepository.save(anneeScolaire);
            return anneeScolaire;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public AnneeScolaire findAnneeScolaireById(Long id) {
        return anneeScolaireRepository.findById(id).orElse(null);
    }

    public List<AnneeScolaire> findAllAnneeScolaire() {
        return anneeScolaireRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public AnneeScolaire findAnneeScolaireEnCours() {
        return anneeScolaireRepository.findByEnCoursIsTrue().orElse(null);
    }

    public void setAllAnneeScolaireEnCoursFalse() {
        List<AnneeScolaire> list = findAllAnneeScolaire();

        if (!list.isEmpty()) {
            list.forEach(l -> {
                l.setEnCours(false);
                anneeScolaireRepository.save(l);
            });
        }
    }

    public AnneeScolaire updateAnneeScolaireEnCours(AnneeScolaire anneeScolaire, boolean status) {
        try {

            setAllAnneeScolaireEnCoursFalse();
            anneeScolaire.setEnCours(status);
            anneeScolaireRepository.save(anneeScolaire);
            return anneeScolaire;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    // ----------------- DOMAINE SERVICES
    public Domaine addDomaine(Domaine domaine) {
        try {
            domaineRepository.save(domaine);
            return domaine;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Domaine findDomaineById(Long id) {
        return domaineRepository.findById(id).orElse(null);
    }

    public List<Domaine> findAllDomaine() {
        return domaineRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- CYCLE SERVICES
    public Cycle addCycle(Cycle cycle) {
        try {
            cycleRepository.save(cycle);
            return cycle;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Cycle findCycleById(Long id) {
        return cycleRepository.findById(id).orElse(null);
    }

    public List<Cycle> findAllCycle() {
        return cycleRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- PARCOURS SERVICES
    public Parcours addParcours(Parcours parcours) {
        try {
            parcoursRepository.save(parcours);
            return parcours;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Parcours findParcoursById(Long id) {
        return parcoursRepository.findById(id).orElse(null);
    }

    public List<Parcours> findAllParcours() {
        return parcoursRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- HORAIRE SERVICES
    public Horaire addHoraire(Horaire horaire) {
        try {
            horaireRepository.save(horaire);
            return horaire;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Horaire findHoraireById(Long id) {
        return horaireRepository.findById(id).orElse(null);
    }

    public List<Horaire> findAllHoraire() {
        return horaireRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- DOCUMENT SERVICES
    public Document addDocument(Document document) {
        try {
            documentRepository.save(document);
            return document;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Document findDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public List<Document> findAllDocument() {
        return documentRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- MENTION SERVICES
    public Mention addMention(Mention mention) {
        try {
            mentionRepository.save(mention);
            return mention;
        } catch(Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Mention findMentionById(Long id) {
        return mentionRepository.findById(id).orElse(null);
    }

    public List<Mention> findMentionByDomaine(Domaine domaine) {
        return mentionRepository.findAllByDomaine(domaine).orElse(new ArrayList<>());
    }

    public List<Mention> findAllMention() {
        return mentionRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }


    // ----------------- PAYS SERVICES
    public List<Pays> findAllPays() {
        return paysRepository.findAll();
    }
}
