package com.ecole.school.repositories;

import com.ecole.school.models.Inscription;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.ProgrammeUEInscription;
import com.ecole.school.models.Semestre;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgrammeUEInscriptionRepository extends JpaRepository<ProgrammeUEInscription, Long> {
    Optional<ProgrammeUEInscription> findByProgrammeUEAndInscriptionAndArchiveFalse(ProgrammeUE programmeUE, Inscription inscription);

    Optional<List<ProgrammeUEInscription>> findAllByArchiveFalseAndInscription(Inscription inscription);

    @Query("SELECT sum(p.programmeUE.credit) FROM ProgrammeUEInscription p where p.programmeUE.semestre=:semestre and p.valide=true and p.inscription=:inscription")
    Integer findSumCreditPrgrammeUeNonValideBySemestre(@Param("semestre") Semestre semestre, @Param("inscription") Inscription inscription);

    @Query("SELECT sum(p.programmeUE.credit) FROM ProgrammeUEInscription p where p.programmeUE.semestre=:semestre and p.inscription=:inscription")
    Integer findSumCreditPrgrammeUeBySemestre(@Param("semestre") Semestre semestre, @Param("inscription") Inscription inscription);
}
