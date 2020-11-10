package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Niveau;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferentielRepository extends JpaRepository<Referentiel, Long> {
    Optional<List<Referentiel>> findAllByArchiveFalse();

    Optional<List<Referentiel>> findAllByNiveauAndArchiveFalse(Niveau niveau);

    Optional<List<Referentiel>> findAllBySpecialiteAndArchiveFalse(Specialite specialite);

    Optional<List<Referentiel>> findAllByNiveauAndSpecialiteAndArchiveFalse(Niveau niveau, Specialite specialite);

    Optional<Referentiel> findByNiveauAndSpecialiteAndAnneeAndArchiveFalse(Niveau niveau, Specialite specialite, int annee);
}
