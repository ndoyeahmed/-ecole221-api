package com.ecole.school.services;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.Semestre;
import com.ecole.school.repositories.SemestreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageSpecialiteService {
    private SemestreRepository semestreRepository;

    @Autowired
    public ParametrageSpecialiteService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }


    // ----------------- SEMESTRE SERVICES
    public Semestre addSemestre(Semestre semestre) {
        try {
            semestreRepository.save(semestre);
            return semestre;
        } catch(Exception e) {
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
}
