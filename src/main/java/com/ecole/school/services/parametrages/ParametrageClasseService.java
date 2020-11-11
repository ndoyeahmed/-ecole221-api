package com.ecole.school.services.parametrages;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Classe;
import com.ecole.school.models.ClasseReferentiel;
import com.ecole.school.models.ClasseSousClasse;
import com.ecole.school.models.Horaire;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Specialite;
import com.ecole.school.repositories.ClasseReferentielRepository;
import com.ecole.school.repositories.ClasseRepository;
import com.ecole.school.repositories.ClasseSousClasseRepository;
import com.ecole.school.repositories.SousClasseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageClasseService {
    private ClasseRepository classeRepository;
    private ClasseSousClasseRepository classeSousClasseRepository;
    private ClasseReferentielRepository classeReferentielRepository;
    private SousClasseRepository sousClasseRepository;

    @Autowired
    public ParametrageClasseService(ClasseRepository classeRepository,
            ClasseSousClasseRepository classeSousClasseRepository,
            ClasseReferentielRepository classeReferentielRepository, SousClasseRepository sousClasseRepository) {
        
        this.classeRepository = classeRepository;
        this.classeSousClasseRepository = classeSousClasseRepository;
        this.classeReferentielRepository = classeReferentielRepository;
        this.sousClasseRepository = sousClasseRepository;
    }

    // ----------------- CLASSE SERVICES
    public Classe addClasse(Classe classe) {
        try {
            classeRepository.save(classe);
            return classe;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Classe findClasseById(Long id) {
        return classeRepository.findById(id).orElse(null);
    }

    public Classe findClasseByNiveauAndSpecialiteAndHoraire(Niveau niveau, Specialite specialite, Horaire horaire) {
        return classeRepository.findByNiveauAndSpecialiteAndHoraireAndArchiveFalse(niveau, specialite, horaire).orElse(null);
    }

    public List<Classe> findAllClasse() {
        return classeRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<Classe> findAllClasseByNiveau(Niveau niveau) {
        return classeRepository.findAllByNiveauAndArchiveFalse(niveau).orElse(new ArrayList<>());
    }

    public List<Classe> findAllClasseBySpecialite(Specialite specialite) {
        return classeRepository.findAllBySpecialiteAndArchiveFalse(specialite).orElse(new ArrayList<>());
    }

    public List<Classe> findAllClasseByHoraire(Horaire horaire) {
        return classeRepository.findAllByHoraireAndArchiveFalse(horaire).orElse(new ArrayList<>());
    }

    public List<Classe> findAllClasseByNiveauAndSpecialite(Niveau niveau, Specialite specialite) {
        return classeRepository.findAllByNiveauAndSpecialiteAndArchiveFalse(niveau, specialite).orElse(new ArrayList<>());
    }

    // ----------------- SOUS CLASSE SERVICES
    public SousClasse addSousClasse(SousClasse sousClasse) {
        try {
            sousClasseRepository.save(sousClasse);
            return sousClasse;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public SousClasse findSousClasseById(Long id) {
        return sousClasseRepository.findById(id).orElse(null);
    }

    public List<SousClasse> findAllSousClasse() {
        return sousClasseRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    public List<SousClasse> findAllSousClasseByNiveau(Niveau niveau) {
        return sousClasseRepository.findAllByNiveauAndArchiveFalse(niveau).orElse(new ArrayList<>());
    }

    public List<SousClasse> findAllSousClasseBySpecialite(Specialite specialite) {
        return sousClasseRepository.findAllBySpecialiteAndArchiveFalse(specialite).orElse(new ArrayList<>());
    }

    public List<SousClasse> findAllSousClasseByNiveauAndSpecialite(Niveau niveau, Specialite specialite) {
        return sousClasseRepository.findAllByNiveauAndSpecialiteAndArchiveFalse(niveau, specialite).orElse(new ArrayList<>());
    }

    // ----------------- CLASSE REFERENTIEL SERVICES
    public ClasseReferentiel addClasseReferentiel(ClasseReferentiel classeReferentiel) {
        try {
            classeReferentielRepository.save(classeReferentiel);
            return classeReferentiel;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public ClasseReferentiel findClasseReferentielById(Long id) {
        return classeReferentielRepository.findById(id).orElse(null);
    }

    public List<ClasseReferentiel> findAllClasseReferentielByClasse(Classe classe) {
        return classeReferentielRepository.findAllByClasseAndArchiveFalse(classe).orElse(new ArrayList<>());
    }

    public List<ClasseReferentiel> findAllClasseReferentielByReferentiel(Referentiel referentiel) {
        return classeReferentielRepository.findAllByReferentielAndArchiveFalse(referentiel).orElse(new ArrayList<>());
    }

    public List<ClasseReferentiel> findAllClasseReferentielByAnneeScolaire(AnneeScolaire anneeScolaire) {
        return classeReferentielRepository.findAllByAnneeScolaireAndArchiveFalse(anneeScolaire).orElse(new ArrayList<>());
    }

    // ----------------- CLASSE SOUS CLASSE SERVICES
    public ClasseSousClasse addClasseSousClasse(ClasseSousClasse classeSousClasse) {
        try {
            classeSousClasseRepository.save(classeSousClasse);
            return classeSousClasse;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public ClasseSousClasse findClasseSousClasseById(Long id) {
        return classeSousClasseRepository.findById(id).orElse(null);
    }

    public List<ClasseSousClasse> findAllClasseSousClasseByClasse(Classe classe) {
        return classeSousClasseRepository.findAllByClasseAndArchiveFalse(classe).orElse(new ArrayList<>());
    }

    public List<ClasseSousClasse> findAllClasseSousClasseBySousClasse(SousClasse sousClasse) {
        return classeSousClasseRepository.findAllBySousClasseAndArchiveFalse(sousClasse).orElse(new ArrayList<>());
    }
}
