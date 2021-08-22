package com.ecole.school.repositories;

import com.ecole.school.models.Inscription;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.ProgrammeUEInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammeUEInscriptionRepository extends JpaRepository<ProgrammeUEInscription, Long> {
    Optional<ProgrammeUEInscription> findByProgrammeUEAndInscriptionAndArchiveFalse(ProgrammeUE programmeUE, Inscription inscription);
}
