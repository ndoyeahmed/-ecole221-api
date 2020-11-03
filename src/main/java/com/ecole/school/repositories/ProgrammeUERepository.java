package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.UE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeUERepository extends JpaRepository<ProgrammeUE, Long> {
    Optional<List<ProgrammeUE>> findAllByArchiveFalse();

    Optional<List<ProgrammeUE>> findAllByUeAndArchiveFalse(UE ue);

    Optional<List<ProgrammeUE>> findAllByReferentielAndArchiveFalse(Referentiel referentiel);

    Optional<List<ProgrammeUE>> findAllBySemestreAndArchiveFalse(Semestre semestre);

    Optional<List<ProgrammeUE>> findAllByReferentielAndSemestreAndArchiveFalse(Referentiel referentiel, Semestre semestre);
}
