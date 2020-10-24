package com.ecole.school.services;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.repositories.AnneeScolaireRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageBaseService {
    private AnneeScolaireRepository anneeScolaireRepository;

    @Autowired
    public ParametrageBaseService(AnneeScolaireRepository anneeScolaireRepository) {
        this.anneeScolaireRepository = anneeScolaireRepository;
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
}
