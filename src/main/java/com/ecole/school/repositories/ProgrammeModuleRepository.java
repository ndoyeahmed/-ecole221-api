package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Module;
import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;

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
}
