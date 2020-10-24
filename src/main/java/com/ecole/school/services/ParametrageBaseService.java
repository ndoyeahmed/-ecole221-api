package com.ecole.school.services;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Domaine;
import com.ecole.school.repositories.AnneeScolaireRepository;
import com.ecole.school.repositories.DomaineRepository;

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

    @Autowired
    public ParametrageBaseService(AnneeScolaireRepository anneeScolaireRepository,
                                DomaineRepository domaineRepository) {
        this.anneeScolaireRepository = anneeScolaireRepository;
        this.domaineRepository = domaineRepository;
    }

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
}
