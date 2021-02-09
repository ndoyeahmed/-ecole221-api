package com.ecole.school.services.inscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Jour;
import com.ecole.school.models.Presence;
import com.ecole.school.repositories.JourRepository;
import com.ecole.school.repositories.PresenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Transactional
@Service
@Log
public class PresenceService {
    private PresenceRepository presenceRepository;
    private JourRepository jourRepository;
    
    @Autowired
    public PresenceService(PresenceRepository presenceRepository, JourRepository jourRepository) {
        this.presenceRepository = presenceRepository;
        this.jourRepository = jourRepository;
    }

    public Presence savePresence(Presence presence) {
        try {
            presenceRepository.save(presence);
            return presence;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Presence findPresenceById(Long id) {
        return presenceRepository.findById(id).orElse(null);
    }

    public List<Presence> saveListPresence(List<Presence> presences) {
        try {
            presenceRepository.saveAll(presences);
            return presences;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Presence findPresenceByInscriptionAndJour(Inscription inscription, Jour jour) {
        try {
            return presenceRepository.findByInscriptionAndJour(inscription, jour).orElse(null);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<Presence> findAllPresenceByInscriptionAndEtatAndAnneeScolaire(Inscription inscription, boolean etat, AnneeScolaire anneeScolaire) {
        return presenceRepository.findAllByInscriptionAndEtatAndAnneeScolaire(inscription, etat, anneeScolaire).orElse(new ArrayList<>());
    }

    public Jour saveJour(Jour jour) {
        try {
            jourRepository.save(jour);
            return jour;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Jour findJourByDate(Date date) {
        try {
            return jourRepository.findByDate(date).orElse(null);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

}
