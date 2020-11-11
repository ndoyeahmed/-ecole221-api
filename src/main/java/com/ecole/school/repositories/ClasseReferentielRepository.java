package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Classe;
import com.ecole.school.models.ClasseReferentiel;
import com.ecole.school.models.Referentiel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseReferentielRepository extends JpaRepository<ClasseReferentiel, Long> {
    Optional<List<ClasseReferentiel>> findAllByClasseAndArchiveFalse(Classe classe);

    Optional<ClasseReferentiel> findByClasseAndReferentielAndArchiveFalse(Classe classe, Referentiel referentiel);

    Optional<List<ClasseReferentiel>> findAllByReferentielAndArchiveFalse(Referentiel referentiel);

    Optional<List<ClasseReferentiel>> findAllByAnneeScolaireAndArchiveFalse(AnneeScolaire anneeScolaire);
}
