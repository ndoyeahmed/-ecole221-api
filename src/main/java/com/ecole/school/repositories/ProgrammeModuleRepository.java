package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.*;

import com.ecole.school.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeModuleRepository extends JpaRepository<ProgrammeModule, Long> {
    Optional<List<ProgrammeModule>> findAllByArchiveFalse();

    @Query("select pm from ProgrammeModule pm where pm.archive=false and pm.programmeUE.id=:programmeUEId")
    Optional<List<ProgrammeModule>> findAllByProgrammeUEAndArchiveFalse(@Param("programmeUEId") Long programmeUEId);

    Optional<List<ProgrammeModule>> findAllByModuleAndArchiveFalse(Module module);

    Optional<List<ProgrammeModule>> findAllByModuleAndProgrammeUEAndArchiveFalse(Module module, ProgrammeUE programmeUE);

    Optional<List<ProgrammeModule>> findAllByArchiveFalseAndProgrammeUE_Referentiel(Referentiel referentiel);

    Optional<List<ProgrammeModule>> findAllByArchiveFalseAndProgrammeUE_ReferentielAndProgrammeUE_Semestre(Referentiel referentiel, Semestre semestre);
}
